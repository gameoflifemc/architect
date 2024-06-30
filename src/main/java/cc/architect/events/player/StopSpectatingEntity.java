package cc.architect.events.player;

import cc.architect.objects.HashMaps;
import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class StopSpectatingEntity implements Listener {
    @EventHandler
    public void onStopSpectatingEntity(PlayerStopSpectatingEntityEvent e) {
        if (HashMaps.DIALOGUE_POSITIONS.containsKey(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
}
