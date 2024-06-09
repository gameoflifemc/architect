package cc.architect.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static cc.architect.managers.Dialogue.playersBeingAligned;

public class Move implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (playersBeingAligned.contains(p)) {
            e.setCancelled(true);
        }
    }
}
