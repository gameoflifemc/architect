package cc.architect.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static cc.architect.managers.Dialogue.playersInDialogue;

public class Move implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (playersInDialogue.contains(p)) {
            e.setCancelled(true);
        }
    }
}
