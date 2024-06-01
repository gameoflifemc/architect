package cc.architect.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static cc.architect.Architect.plugin;
import static cc.architect.Utilities.alignRotation;
import static org.bukkit.Bukkit.getScheduler;

public class Move implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!e.hasChangedOrientation()) {
            return;
        }
        Player p = e.getPlayer();
        getScheduler().runTaskLater(plugin, () -> {
            float yaw = p.getYaw();
            float pitch = p.getPitch();
            getScheduler().runTaskLater(plugin, () -> {
                if (p.getYaw() == yaw && p.getPitch() == pitch) {
                    alignRotation(p,0,0);
                }
            },10);
        },10);
    }
}
