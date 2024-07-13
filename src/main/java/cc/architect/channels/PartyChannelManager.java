package cc.architect.channels;

import cc.architect.Utilities;
import cc.architect.managers.Party;
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

import static cc.architect.channels.BaseChannels.*;
import static cc.architect.channels.ServerName.getServerName;
import static cc.architect.managers.Party.hasInvite;
import static org.bukkit.Bukkit.getPlayerExact;

public class PartyChannelManager implements PluginMessageListener {

    public static Map<String, ImmutablePair<String,String>> playerInvitesQueue = new HashMap<>();

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!channel.equals(PUBLIC_CHANNEL)) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        //Bukkit.broadcastMessage("Received message from BungeeCord on subchannel: " + subchannel);

        if(subchannel.equals(INVITE_CHANNEL)){
            processInviteChannel(getForwardMessageData(in));
            return;
        }

        /*DataInputStream msgin = getForwardMessageData(in);

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
     * - Channel: INVITE_CHANNEL
     * **/
    public static void sendRemoteInviteRequest(Player inviteSender, String inviteReceiver){
        BaseChannels.prepareForwardMessage();
        DataOutputStream out = getForwardMessageData();
        try {
            String uuid = UUID.randomUUID().toString();
            out.writeUTF(REQUEST);
            out.writeUTF(uuid);
            out.writeUTF(getServerName());
            out.writeUTF(inviteReceiver);
            out.writeUTF(inviteSender.getName());

            playerInvitesQueue.put(uuid, new ImmutablePair<>(inviteSender.getName(),inviteReceiver));
        } catch (IOException e) {
            inviteSender.sendMessage(Messages.SEND_INVITE_ERROR);
            return;
        }
        BaseChannels.sendForwardMessage("ALL", INVITE_CHANNEL, inviteSender);
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
     * - Channel: INVITE_CHANNEL
     * **/
    public static void processInviteRequest(DataInputStream messageData, String uuid, String serverName){
        String inviteReceiver = Utilities.readUTF(messageData);
        String inviteSender = Utilities.readUTF(messageData);

        String response = null;

        if(getPlayerExact(inviteReceiver)==null) return;
        if(!getPlayerExact(inviteReceiver).isOnline()) response = "OFFLINE";

        if(response == null) {
            response = hasInvite(inviteReceiver) ? "DENY" : "ACCEPT";
        }
        if(response.equals("ACCEPT")){
            Party.createInvite(inviteReceiver, inviteSender,getServerName(),serverName);
        }

        BaseChannels.prepareForwardMessage();
        DataOutputStream messageOut = BaseChannels.getForwardMessageData();

        try {
            messageOut.writeUTF(RESPONSE);
            messageOut.writeUTF(uuid);
            messageOut.writeUTF(getServerName());
            messageOut.writeUTF(response);
            messageOut.writeUTF(inviteReceiver);
            messageOut.writeUTF(inviteSender);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BaseChannels.sendForwardMessage(serverName, INVITE_CHANNEL, getPlayerExact(inviteReceiver));

    }
    public static void processInviteResponse(DataInputStream messageData, String uuid, String serverName){
        String response = Utilities.readUTF(messageData);
        String inviteReceiver = Utilities.readUTF(messageData);
        String inviteSender = Utilities.readUTF(messageData);

        ImmutablePair<String,String> pair = playerInvitesQueue.get(uuid);
        if(pair==null) return;
        if(!pair.getLeft().equals(inviteSender) || !pair.getRight().equals(inviteReceiver)) return;
        playerInvitesQueue.remove(uuid);

        if(response.equals("ACCEPT")){
            Party.sendInviteMessages(inviteReceiver, inviteSender);
            return;
        }
        if(response.equals("DENY")){
            getPlayerExact(inviteSender).sendMessage(Messages.SEND_INVITE_PLAYER_HAS_INVITE(inviteReceiver));
            return;
        }
        if(response.equals("OFFLINE")){
            getPlayerExact(inviteSender).sendMessage(Messages.PLAYER_NOT_ONLINE(inviteReceiver));
            return;
        }
    }
}
