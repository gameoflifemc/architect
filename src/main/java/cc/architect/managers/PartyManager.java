package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.objects.Messages;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static cc.architect.channels.PartyChannelManager.sendRemoteInviteRequest;
import static de.oliver.fancynpcs.libs.chatcolorhandler.ChatColorHandler.sendMessage;
import static org.bukkit.Bukkit.getPlayerExact;

public class PartyManager {
    public static final int MAX_INVITE_TIME = 60; // 60 seconds
    public static Map<String, ImmutablePair<String,Integer>> invites = new HashMap<>();
    public static void sendInvite(Player sender, String receiverName) {
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
        int deleteTaskId = Bukkit.getScheduler().runTaskLater(Architect.PLUGIN, () -> {
            invites.remove(receiver);
            optionMessageSend(sender,Messages.SEND_INVITE_EXPIRED);
            optionMessageSend(receiver,Messages.SEND_INVITE_EXPIRED);
        }, 20 * MAX_INVITE_TIME).getTaskId();

        //creates invite
        invites.put(receiver, new ImmutablePair<>(sender, deleteTaskId));
        sendInviteMessages(receiver, sender);
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
        if(!hasInvite(receiver)) return;

        //accepts invite messages
        ImmutablePair<String,Integer> invite = invites.get(receiver);
        String sender = invite.getLeft();
        invites.remove(receiver);
        Bukkit.getScheduler().cancelTask(invite.getRight());

        optionMessageSend(sender,Messages.SEND_INVITE_ACCEPTED(receiver));
        optionMessageSend(receiver, Messages.SEND_INVITE_ACCEPT(sender));
    }

    public static void denyInvite(String receiver) {
        //checks if player has invite
        if(!hasInvite(receiver)) return;

        //deny invite
        ImmutablePair<String,Integer> invite = invites.get(receiver);
        String sender = invite.getLeft();
        invites.remove(receiver);
        Bukkit.getScheduler().cancelTask(invite.getRight());
        optionMessageSend(sender,Messages.SEND_INVITE_DENIED(receiver));
        optionMessageSend(receiver,Messages.SEND_INVITE_DENY(sender));
    }

    public static boolean hasInvite(String receiver) {
        if(!invites.containsKey(receiver)){
            return false;
        }
        return true;
    }

    public static void optionMessageSend(String player, Component message){
        if(getPlayerExact(player)!=null)getPlayerExact(player).sendMessage(message);
    }
}
