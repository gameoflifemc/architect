package cc.architect.events.player;

import cc.architect.Architect;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Teleport implements Listener {
    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getCause().equals(PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT)) {
            if (e.getFrom().getWorld().getName().equals("mine")) {
                e.setTo(new Location(Architect.MINE,12,-29,-16));
            } else {
                e.setCancelled(true);
            }
        }
    }
}
