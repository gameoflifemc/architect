package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.channels.Base;
import cc.architect.objects.Messages;
import cc.architect.objects.PartyInvite;
import com.google.common.io.ByteArrayDataOutput;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static cc.architect.channels.Base.*;
import static cc.architect.channels.Party.sendRemoteInviteRequest;
import static org.bukkit.Bukkit.getPlayerExact;

public class Parties {
    public static final int MAX_INVITE_TIME = 60; // 60 seconds
    //receiver -> sender, taskid
    public static final Map<String, PartyInvite> invites = new HashMap<>();
    public static void sendInvite(Player sender, String receiverName) {
        Player receiver = getPlayerExact(receiverName);
        //checks if player is sending invite to themselves
        if (sender.getName().equals(receiverName)) {
            sender.sendMessage(Messages.SEND_INVITE_SELF);
            return;
        }
        if (receiver == null) {
            sendRemoteInviteRequest(sender, receiverName);
        } else {
            sender.sendMessage(Messages.PLAYER_ON_SAME_SERVER);
        }
        //checks if player has invite
    }
    /**
     * Creates invite for player
     * @param receiver Player receiving the invite
     * @param sender Player sending the invite
     **/
    public static void createInvite(String receiver, String sender){
        //creates task for deletion of invite after expiration
        int deleteTaskId = createDeleteTask(receiver, sender);
        //creates invite
        invites.put(receiver, new PartyInvite(sender, deleteTaskId));
        sendInviteMessages(receiver, sender);
    }
    public static void createInvite(String receiver, String sender, String receiverServer, String senderServer){
        //creates task for deletion of invite after expiration
        int deleteTaskId = createDeleteTask(receiver, sender);
        //creates invite
        invites.put(receiver.toLowerCase(), new PartyInvite(sender, deleteTaskId, receiverServer, senderServer));
        sendInviteMessages(receiver, sender);
    }
    public static int createDeleteTask(String receiver, String sender){
        return Architect.SCHEDULER.runTaskLater(Architect.PLUGIN, () -> {
            invites.remove(receiver);
            optionMessageSend(sender,Messages.SEND_INVITE_EXPIRED);
            optionMessageSend(receiver,Messages.SEND_INVITE_EXPIRED);
        }, 20 * MAX_INVITE_TIME).getTaskId();
    }
    /**
     * Sends invite messages to players
     * @param receiver Player receiving the invite
     * @param sender Player sending the invite
     */
    public static void sendInviteMessages(String receiver, String sender){
        optionMessageSend(sender,Messages.SEND_INVITE_SENDER(receiver));
        optionMessageSend(receiver,Messages.SEND_INVITE_RECEIVER(sender));
    }
    public static void acceptInvite(String receiver) {
        //checks if player has invite
        if (!hasInvite(receiver)) {
            getPlayerExact(receiver).sendMessage(Messages.NO_INVITE);
            return;
        }
        //accepts invite messages
        PartyInvite invite = invites.get(receiver.toLowerCase());
        String sender = invite.getSender();
        Bukkit.getScheduler().cancelTask(invite.getTaskID());
        if(invite.isSameServer()) {
            sameServerAcceptHandler(receiver, sender, invite);
        } else {
            otherServerAcceptHandler(receiver, sender, invite);
        }
        optionMessageSend(sender,Messages.SEND_INVITE_ACCEPTED(receiver));
        optionMessageSend(receiver, Messages.SEND_INVITE_ACCEPT(sender));
        Architect.SCHEDULER.cancelTask(invite.getTaskID());
        invites.remove(receiver.toLowerCase());
    }

    public static void denyInvite(String receiver) {
        //checks if player has invite
        if (!hasInvite(receiver)) {
            getPlayerExact(receiver).sendMessage(Messages.NO_INVITE);
            return;
        }
        //deny invite
        PartyInvite invite = invites.get(receiver.toLowerCase());
        String sender = invite.getSender();
        invites.remove(receiver);
        Bukkit.getScheduler().cancelTask(invite.getTaskID());

        //send deny message to sender
        ByteArrayDataOutput out = getBasicMessage(Base.RAW_MESSAGE);
        //player
        out.writeUTF(invite.getSender());
        //message
        out.writeUTF(JSONComponentSerializer.json().serialize(Messages.SEND_INVITE_DENIED(receiver)));
        sendToDefaultChannel(out);

        optionMessageSend(sender,Messages.SEND_INVITE_DENIED(receiver));
        optionMessageSend(receiver,Messages.SEND_INVITE_DENY(sender));
        Architect.SCHEDULER.cancelTask(invite.getTaskID());
        invites.remove(receiver.toLowerCase());
    }
    public static boolean hasInvite(String receiver) {
        return invites.containsKey(receiver.toLowerCase());
    }
    public static void optionMessageSend(String player, Component message){
        Player p = getPlayerExact(player);
        if (p != null) {
            p.sendMessage(message);
        }
    }
    public static void sameServerAcceptHandler(String receiver, String sender, PartyInvite invite){
        Player pReceiver = getPlayerExact(receiver);
        if (pReceiver == null) {
            return;
        }
        Player pSender = getPlayerExact(sender);
        if (pSender == null) {
            return;
        }
        pReceiver.teleport(pSender.getLocation());
    }
    public static void otherServerAcceptHandler(String receiver, String sender, PartyInvite invite){
        // sends message about teleporting the player on the server
        // plus the server name of the player that is being teleported for PARTIES and CANNOT_MAKE_PARTY
        //Base.sendForwardMessage(invite.getSenderServer(), Base.TELEPORT);
        // connecting to other server
        ByteArrayDataOutput out = getBasicMessage(Base.CONNECT);
        out.writeUTF(invite.getSenderServer());
        Player p = getPlayerExact(receiver);
        if (p == null) {
            return;
        }
        sendToDefaultChannelPlayer(out,p);
    }
}
