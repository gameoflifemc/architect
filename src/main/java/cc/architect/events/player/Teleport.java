package cc.architect.events.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Teleport implements Listener {
    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getCause().equals(PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT)) {
            World world = Bukkit.getWorld("mine");
            if (e.getFrom().getWorld().equals(world)) {
                e.setTo(new Location(world,12,0,-12));
            } else {
                e.setCancelled(true);
            }
            
        }
    }
}
