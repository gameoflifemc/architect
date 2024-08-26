package cc.architect.events.player;

import cc.architect.managers.Compasses;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Move implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (e.hasChangedBlock() && !p.getWorld().getName().equals("world")) {
            Compasses.updateLocations(p);
        }
    }
}
