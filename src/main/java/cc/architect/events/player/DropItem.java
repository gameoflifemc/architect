package cc.architect.events.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropItem implements Listener {
    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        // disable dropping items
        e.setCancelled(true);
    }
}
