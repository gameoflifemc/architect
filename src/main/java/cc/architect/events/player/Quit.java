package cc.architect.events.player;

import cc.architect.managers.Meta;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Quit implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        // empty quit message
        e.quitMessage(Component.empty());
        // get player
        Player p = e.getPlayer();
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
