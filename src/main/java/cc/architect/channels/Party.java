package cc.architect.channels;

import cc.architect.Utilities;
import cc.architect.managers.Parties;
import cc.architect.objects.Messages;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static cc.architect.channels.Base.getForwardMessageData;
import static cc.architect.channels.ServerName.getServerName;
import static cc.architect.managers.Parties.hasInvite;
import static org.bukkit.Bukkit.getPlayerExact;

public class Party implements PluginMessageListener {
    public static final Map<String, ImmutablePair<String,String>> playerInvitesQueue = new HashMap<>();
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!channel.equals(Base.PUBLIC)) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();
        if (subChannel.equals(Base.INVITE)) {
            processInviteChannel(getForwardMessageData(in));
        }
    }
    /**
     * Sends request of invite to another player on the network
     * @param inviteSender Player sending the invite
     * @param inviteReceiver Player receiving the invite
     * <br><br>
     * REQUEST FORMAT: <br>
     * - Type: Request <br>
     * - UUID: Special identificator <br>
     * - ServerName: Name of the server sending the request <br>
     * - Receiver: Name of the player receiving the request <br>
     * - Sender: Name of the player sending the request <br><br>
     * - Channel: BaseChannels.INVITE_CHANNEL
     * **/
    public static void sendRemoteInviteRequest(Player inviteSender, String inviteReceiver){
        Base.prepareForwardMessage();
        DataOutputStream out = getForwardMessageData();
        try {
            String uuid = UUID.randomUUID().toString();
            out.writeUTF(Base.REQUEST);
            out.writeUTF(uuid);
            out.writeUTF(getServerName());
            out.writeUTF(inviteReceiver);
            out.writeUTF(inviteSender.getName());

            playerInvitesQueue.put(uuid, new ImmutablePair<>(inviteSender.getName(),inviteReceiver));
        } catch (IOException e) {
            inviteSender.sendMessage(Messages.SEND_INVITE_ERROR);
            return;
        }
        Base.sendForwardMessage("ALL", Base.INVITE, inviteSender);
    }
    public static void processInviteChannel(DataInputStream messageData){
        String type = Utilities.readUTF(messageData);
        String uuid = Utilities.readUTF(messageData);
        String serverName = Utilities.readUTF(messageData);

        if(type.equals(Base.REQUEST)){
            processInviteRequest(messageData, uuid, serverName);
            return;
        }
        if(type.equals(Base.RESPONSE)){
            processInviteResponse(messageData, uuid, serverName);
        }
    }
    /**
     * Sends response of invite from another player on the network
     * <br><br>
     * RESPONSE FORMAT: <br>
     * - Type: responce <br>
     * - UUID: Special identificator <br>
     * - ServerName: Name of the server sending the request <br>
     * - Response: DENY-has invite  ACCEPT-valid NOT_FOUND OFFLINE<br>
     * - Receiver: Name of the player receiving the request <br>
     * - Sender: Name of the player sending the request <br><br>
     * - Channel: BaseChannels.INVITE_CHANNEL
     * **/
    public static void processInviteRequest(DataInputStream messageData, String uuid, String serverName){
        String inviteReceiver = Utilities.readUTF(messageData);
        String inviteSender = Utilities.readUTF(messageData);
        String response = null;
        
        Player p = getPlayerExact(inviteReceiver);
        if (p == null) {
            return;
        }
        if (!p.isOnline()) {
            response = "OFFLINE";
        }
        if (response == null) {
            response = hasInvite(inviteReceiver) ? "DENY" : "ACCEPT";
        }
        if (response.equals("ACCEPT")){
            Parties.createInvite(inviteReceiver, inviteSender,getServerName(),serverName);
        }
        Base.prepareForwardMessage();
        DataOutputStream messageOut = Base.getForwardMessageData();
        try {
            messageOut.writeUTF(Base.RESPONSE);
            messageOut.writeUTF(uuid);
            messageOut.writeUTF(getServerName());
            messageOut.writeUTF(response);
            messageOut.writeUTF(inviteReceiver);
            messageOut.writeUTF(inviteSender);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Base.sendForwardMessage(serverName, Base.INVITE,p);
    }
    public static void processInviteResponse(DataInputStream messageData, String uuid, String serverName){
        String response = Utilities.readUTF(messageData);
        String inviteReceiver = Utilities.readUTF(messageData);
        String inviteSender = Utilities.readUTF(messageData);
        ImmutablePair<String,String> pair = playerInvitesQueue.get(uuid);
        if (pair == null) {
            return;
        }
        if (!pair.getLeft().equals(inviteSender) || !pair.getRight().equals(inviteReceiver)) {
            return;
        }
        playerInvitesQueue.remove(uuid);
        Player p = getPlayerExact(inviteSender);
        if (p == null) {
            return;
        }
        switch (response) {
            case "ACCEPT" -> Parties.sendInviteMessages(inviteReceiver, inviteSender);
            case "DENY" -> p.sendMessage(Messages.SEND_INVITE_PLAYER_HAS_INVITE(inviteReceiver));
            case "OFFLINE" -> p.sendMessage(Messages.PLAYER_NOT_ONLINE(inviteReceiver));
        }
    }
}
