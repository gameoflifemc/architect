package cc.architect.tasks.player;

import cc.architect.managers.Meta;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Autosave implements Runnable {
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(Autosave::lastLocation);
    }
    public static void lastLocation(Player p) {
        // check world
        String world = p.getWorld().getName();
        // don't save last location if the player isn't in game
        if (world.equals("world")) {
            return;
        }
        // get last location
        Location loc = p.getLocation();
        // turn location into data
        String data = world + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
        // save player location
        Meta.set(p,"last_location",data);
    }
}
