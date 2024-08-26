package cc.architect.events.player;

import cc.architect.Architect;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class SpawnLocation implements Listener {
    @EventHandler
    public void onSpawnLocation(PlayerSpawnLocationEvent e) {
        // teleport to spawn location
        e.setSpawnLocation(Architect.WORLD.getSpawnLocation().add(0.5,0,0.5));
    }
}
