package cc.architect.events.player;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Respawn implements Listener {
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        World world = Bukkit.getWorld("village");
        if (world == null) {
            return;
        }
        e.setRespawnLocation(world.getSpawnLocation());
    }
}
