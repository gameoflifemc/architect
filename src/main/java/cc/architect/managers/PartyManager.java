package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.objects.Messages;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PartyManager {
    public static final int MAX_INVITE_TIME = 60; // 60 seconds
    public static Map<Player, ImmutablePair<Player,Integer>> invites = new HashMap<>();
    public static void sendInvite(Player sender, String receiverName) {
        Player receiver = Bukkit.getPlayer(receiverName);

        if (receiver == null) {
            Bukkit.broadcastMessage("name "+receiverName+" not found on this server");
            return;
        }

        //checks if player is sending invite to themselves
        if (sender.equals(receiver)) {
            sender.sendMessage(Messages.SEND_INVITE_SELF);
            return;
        }

        //checks if player has invite
        if(invites.containsKey(receiver)){
            sender.sendMessage(Messages.SEND_INVITE_PLAYER_HAS_INVITE(receiver));
            return;
        }

        //creates task for deletion of invite after expiration
        int deleteTaskId = Bukkit.getScheduler().runTaskLater(Architect.PLUGIN, () -> {
            invites.remove(receiver);
            sender.sendMessage(Messages.SEND_INVITE_EXPIRED);
            receiver.sendMessage(Messages.SEND_INVITE_EXPIRED);
        }, 20 * MAX_INVITE_TIME).getTaskId();

        //creates invite
        invites.put(receiver, new ImmutablePair<>(sender, deleteTaskId));
        sender.sendMessage(Messages.SEND_INVITE_SENDER(receiver));
        receiver.sendMessage(Messages.SEND_INVITE_RECEIVER(sender));
    }
    public static void acceptInvite(Player receiver) {
        //checks if player has invite
        if(!hasInvite(receiver)) return;

        //accepts invite messages
        ImmutablePair<Player,Integer> invite = invites.get(receiver);
        Player sender = invite.getLeft();
        invites.remove(receiver);
        Bukkit.getScheduler().cancelTask(invite.getRight());
        sender.sendMessage(Messages.SEND_INVITE_ACCEPTED(receiver));
        receiver.sendMessage(Messages.SEND_INVITE_ACCEPT(sender));
    }

    public static void denyInvite(Player receiver) {
        //checks if player has invite
        if(!hasInvite(receiver)) return;

        //deny invite
        ImmutablePair<Player,Integer> invite = invites.get(receiver);
        Player sender = invite.getLeft();
        invites.remove(receiver);
        Bukkit.getScheduler().cancelTask(invite.getRight());
        sender.sendMessage(Messages.SEND_INVITE_DENIED(receiver));
        receiver.sendMessage(Messages.SEND_INVITE_DENY(sender));
    }

    public static boolean hasInvite(Player receiver) {
        if(!invites.containsKey(receiver)){
            receiver.sendMessage(Messages.SEND_INVITE_NONE);
            return false;
        }
        return true;
    }
}
