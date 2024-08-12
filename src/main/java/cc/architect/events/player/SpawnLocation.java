package cc.architect.events.player;

import cc.architect.managers.Meta;
import cc.architect.managers.Routines;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class SpawnLocation implements Listener {
    public static final String META = "last_location";
    @EventHandler
    public void onSpawnLocation(PlayerSpawnLocationEvent e) {
        // prepare location and player
        Location loc;
        Player p = e.getPlayer();
        // check if player is already in game
        if (Meta.check(p,META)) {
            // teleport to last location
            String[] data = Meta.get(p,META).split(",");
            World world = Bukkit.getWorld(data[0]);
            if (world == null) {
                return;
            }
            loc = new Location(world,Double.parseDouble(data[1]),Double.parseDouble(data[2]),Double.parseDouble(data[3]),Float.parseFloat(data[4]),Float.parseFloat(data[5]));
            // continue game
            Routines.continueGame(p);
        } else {
            // teleport to spawn location
            World world = Bukkit.getWorld("world");
            if (world == null) {
                return;
            }
            loc = world.getSpawnLocation();
        }
        e.setSpawnLocation(loc);
    }
}
