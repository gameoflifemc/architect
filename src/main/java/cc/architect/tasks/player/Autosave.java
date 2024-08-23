package cc.architect.tasks.player;

import cc.architect.managers.Meta;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;

public class Autosave implements Runnable {
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            location(p);
            emeralds(p);
        });
    }
    public static void location(Player p) {
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
    public static void emeralds(Player p) {
        // create atomic integer for emeralds
        AtomicInteger emeralds = new AtomicInteger();
        emeralds.set(0);
        // count emeralds in inventory
        for (ItemStack stack : p.getInventory().getContents()) {
            if (stack != null && stack.getType() == Material.EMERALD) {
                emeralds.getAndAdd(stack.getAmount());
            }
        }
        // save emeralds
        Meta.set(p,"emeralds_total", String.valueOf(emeralds.get()));
    }
}
