package cc.architect.events.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class SpawnLocation implements Listener {
    @EventHandler
    public void onSpawnLocation(PlayerSpawnLocationEvent e) {
        World world = Bukkit.getWorld("foyer");
        Location location = new Location(world,0.5,127.0,0.5,0,0);
        e.setSpawnLocation(location);
    }
}
