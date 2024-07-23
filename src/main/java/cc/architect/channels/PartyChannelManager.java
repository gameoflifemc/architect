package cc.architect.channels;

import cc.architect.Utilities;
import cc.architect.managers.PartyManager;
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

import static cc.architect.channels.BaseChannels.getForwardMessageData;
import static cc.architect.channels.ServerName.getServerName;
import static cc.architect.managers.PartyManager.hasInvite;
import static cc.architect.objects.HashMaps.IS_IN_PARTY;
import static org.bukkit.Bukkit.getPlayerExact;

public class PartyChannelManager implements PluginMessageListener {
    public static Map<String, ImmutablePair<String,String>> playerInvitesQueue = new HashMap<>();
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!channel.equals(BaseChannels.PUBLIC)) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();
        //Bukkit.broadcastMessage("Received message from BungeeCord on subchannel: " + subchannel);
        if (subChannel.equals(BaseChannels.INVITE)) {
            processInviteChannel(getForwardMessageData(in));
        }
        /* DataInputStream msgin = getForwardMessageData(in);

        try {
            String somedata = msgin.readUTF();
            //Bukkit.broadcastMessage("Received message with data: " + somedata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
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
        BaseChannels.prepareForwardMessage();
        DataOutputStream out = getForwardMessageData();
        try {
            String uuid = UUID.randomUUID().toString();
            out.writeUTF(BaseChannels.REQUEST);
            out.writeUTF(uuid);
            out.writeUTF(getServerName());
            out.writeUTF(inviteReceiver);
            out.writeUTF(inviteSender.getName());

            playerInvitesQueue.put(uuid, new ImmutablePair<>(inviteSender.getName(),inviteReceiver));
        } catch (IOException e) {
            inviteSender.sendMessage(Messages.SEND_INVITE_ERROR);
            return;
        }
        BaseChannels.sendForwardMessage("ALL", BaseChannels.INVITE, inviteSender);
    }
    public static void processInviteChannel(DataInputStream messageData){
        String type = Utilities.readUTF(messageData);
        String uuid = Utilities.readUTF(messageData);
        String serverName = Utilities.readUTF(messageData);

        if(type.equals("Request")){
            processInviteRequest(messageData, uuid, serverName);
            return;
        }
        if(type.equals("Response")){
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
     * - Response: DENY-has invite  ACCEPT-valid NOT_FOUND OFFILNE<br>
     * - Receiver: Name of the player receiving the request <br>
     * - Sender: Name of the player sending the request <br><br>
     * - Channel: BaseChannels.INVITE_CHANNEL
     * **/
    public static void processInviteRequest(DataInputStream messageData, String uuid, String serverName){
        String inviteReceiver = Utilities.readUTF(messageData);
        String inviteSender = Utilities.readUTF(messageData);
        String response = null;

        if(IS_IN_PARTY.contains(inviteReceiver)){
            response = "CAN_NOT_MAKE_PARTY";
        }
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
            PartyManager.createInvite(inviteReceiver, inviteSender,getServerName(),serverName);
        }
        BaseChannels.prepareForwardMessage();
        DataOutputStream messageOut = BaseChannels.getForwardMessageData();
        try {
            messageOut.writeUTF(BaseChannels.RESPONSE);
            messageOut.writeUTF(uuid);
            messageOut.writeUTF(getServerName());
            messageOut.writeUTF(response);
            messageOut.writeUTF(inviteReceiver);
            messageOut.writeUTF(inviteSender);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BaseChannels.sendForwardMessage(serverName,BaseChannels.INVITE,p);
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
            case "ACCEPT" -> {
                PartyManager.sendInviteMessages(inviteReceiver, inviteSender);
            }
            case "DENY" -> {
                p.sendMessage(Messages.SEND_INVITE_PLAYER_HAS_INVITE(inviteReceiver));
            }
            case "OFFLINE" -> {
                p.sendMessage(Messages.PLAYER_NOT_ONLINE(inviteReceiver));
            }
            case "CAN_NOT_MAKE_PARTY" -> {
                p.sendMessage(Messages.PLAYER_IN_PARTY);
            }
        }
    }
}
