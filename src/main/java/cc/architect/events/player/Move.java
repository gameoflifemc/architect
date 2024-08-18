package cc.architect.events.player;

import cc.architect.managers.Compasses;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class Move implements Listener {
    private static final HashMap<Player, Float> DIFFERENCES = new HashMap<>();
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        float difference = e.getFrom().getYaw() - e.getTo().getYaw();
        if (difference == 0) {
            return;
        }
        Player p = e.getPlayer();
        if (DIFFERENCES.containsKey(p)) {
            difference += DIFFERENCES.get(p);
        }
        if (difference >= 1 || difference <= -1) {
            Compasses.update(p,(int) difference);
            DIFFERENCES.remove(p);
            return;
        }
        DIFFERENCES.put(p,difference);
    }
}
