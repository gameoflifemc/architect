package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.channels.BaseChannels;
import cc.architect.objects.Messages;
import cc.architect.objects.PartyHolder;
import cc.architect.objects.PartyInvite;
import com.google.common.io.ByteArrayDataOutput;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static cc.architect.channels.BaseChannels.getBasicMessage;
import static cc.architect.channels.BaseChannels.sendToDefaultChannelPlayer;
import static cc.architect.channels.PartyChannelManager.sendRemoteInviteRequest;
import static cc.architect.objects.HashMaps.IS_IN_PARTY;
import static cc.architect.objects.HashMaps.PARTIES;
import static cc.architect.objects.PartyHolder.getMemberParty;
import static org.bukkit.Bukkit.getPlayerExact;

public class Parties {
    public static final int MAX_INVITE_TIME = 60; // 60 seconds
    //receiver -> sender, taskid
    public static final Map<String, PartyInvite> invites = new HashMap<>();
    public static void sendInvite(Player sender, String receiverName) {
        /*if(IS_IN_PARTY.contains(sender.getName())){
            sender.sendMessage(Messages.SEND_INVITE_CANNOT_MAKE_PARTY);
            return;
        }
        if(IS_IN_PARTY.contains(receiverName)){
            sender.sendMessage(Messages.PLAYER_IN_PARTY);
            return;
        }*/
        Player receiver = getPlayerExact(receiverName);
        //checks if player is sending invite to themselves
        if (sender.getName().equals(receiverName)) {
            sender.sendMessage(Messages.SEND_INVITE_SELF);
            return;
        }
        if (receiver == null) {
            sendRemoteInviteRequest(sender, receiverName);
            return;
        }
        //checks if player has invite
        if(invites.containsKey(receiver.getName())){
            sender.sendMessage(Messages.SEND_INVITE_PLAYER_HAS_INVITE(receiver.getName()));
            return;
        }
        createInvite(receiver.getName(), sender.getName());
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
        invites.put(receiver, new PartyInvite(sender, deleteTaskId, receiverServer, senderServer));
        sendInviteMessages(receiver, sender);
    }
    public static int createDeleteTask(String receiver, String sender){
        return Bukkit.getScheduler().runTaskLater(Architect.PLUGIN, () -> {
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
            return;
        }
        //accepts invite messages
        PartyInvite invite = invites.get(receiver);
        String sender = invite.getSender();
        Bukkit.getScheduler().cancelTask(invite.getTaskID());
        if(invite.isSameServer()) {
            sameServerAcceptHandler(receiver, sender, invite);
        } else {
            otherServerAcceptHandler(receiver, sender, invite);
        }
        optionMessageSend(sender,Messages.SEND_INVITE_ACCEPTED(receiver));
        optionMessageSend(receiver, Messages.SEND_INVITE_ACCEPT(sender));
        invites.remove(receiver);
    }

    public static void denyInvite(String receiver) {
        //checks if player has invite
        if (!hasInvite(receiver)) {
            return;
        }
        //deny invite
        PartyInvite invite = invites.get(receiver);
        String sender = invite.getSender();
        invites.remove(receiver);
        Bukkit.getScheduler().cancelTask(invite.getTaskID());
        optionMessageSend(sender,Messages.SEND_INVITE_DENIED(receiver));
        optionMessageSend(receiver,Messages.SEND_INVITE_DENY(sender));
    }

    public static void leaveParty(String member){
        if (!IS_IN_PARTY.contains(member)) {
            Player p = getPlayerExact(member);
            if (p != null) {
                p.sendMessage(Messages.NOT_IN_PARTY);
            }
            return;
        }
        /*PartyHolder party = PARTIES.get(member);*/
        if (PARTIES.containsKey(member)) {
            PartyHolder party = PARTIES.get(member);
            party.getMembers().forEach((partyMemberName) -> {
                Player partyMember = getPlayerExact(partyMemberName);
                if (partyMember != null) {
                    partyMember.sendMessage(Messages.YOU_LEFT_PARTY_LEADER_LEAVE);
                }
                IS_IN_PARTY.remove(partyMemberName);
            });
            PARTIES.remove(member);
            IS_IN_PARTY.remove(member);
            return;
        }
        IS_IN_PARTY.remove(member);
        PartyHolder party = getMemberParty(member);
        if (party == null) {
            return;
        }
        party.removeMember(member);
        Player p = getPlayerExact(member);
        if (p != null) {
            p.sendMessage(Messages.YOU_LEFT_PARTY);
        }
        if (party.getMembers().isEmpty()) {
            Player leader = getPlayerExact(party.getLeader());
            if (leader != null) {
                leader.sendMessage(Messages.YOU_LEFT_PARTY_MEMBERS_EMPTY);
            }
            IS_IN_PARTY.remove(party.getLeader());
            PARTIES.remove(member);
        }
    }
    public static boolean hasInvite(String receiver) {
        return invites.containsKey(receiver);
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
        setPartiesMap(receiver, sender);
    }
    public static void otherServerAcceptHandler(String receiver, String sender, PartyInvite invite){
        //sends message about teleporting the player on the server
        //plus the server name of the player that is being teleported for PARTIES and CANNOT_MAKE_PARTY
        BaseChannels.prepareForwardMessage();
        DataOutputStream messageOut = BaseChannels.getForwardMessageData();
        try {
            messageOut.writeUTF(receiver);
            messageOut.writeUTF(sender);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BaseChannels.sendForwardMessage(invite.getSenderServer(), BaseChannels.TELEPORT);
        //connecting to other server
        ByteArrayDataOutput out = getBasicMessage(BaseChannels.CONNECT);
        out.writeUTF(invite.getSenderServer());
        Bukkit.broadcastMessage(invite.getSenderServer());
        Player p = getPlayerExact(receiver);
        if (p == null) {
            return;
        }
        sendToDefaultChannelPlayer(out,p);
    }
    public static void setPartiesMap(String receiver, String sender){
        IS_IN_PARTY.add(receiver);
        IS_IN_PARTY.add(sender);
        if(!PARTIES.containsKey(sender)) {
            PARTIES.put(sender, new PartyHolder(sender, receiver));
        }else{
            PARTIES.get(sender).addMember(receiver);
        }
        Bukkit.broadcastMessage(PARTIES.get(sender).toString());
    }
}
