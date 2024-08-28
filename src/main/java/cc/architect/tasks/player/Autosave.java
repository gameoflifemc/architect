package cc.architect.tasks.player;

import cc.architect.managers.Facts;
import cc.architect.managers.Meta;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;

public class Autosave implements Runnable {
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(Autosave::autosave);
    }
    public static void autosave(Player p) {
        // don't autosave if player is in lobby
        if (p.getWorld().getName().equals("world")) {
            return;
        }
        location(p);
        time(p);
        emeralds(p);
        facts(p);
        calculateDaily(p);
    }
    private static void location(Player p) {
        // get last location
        Location loc = p.getLocation();
        // turn location into data
        String data = loc.getWorld() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
        // save player location
        Meta.set(p,Meta.LAST_LOCATION,data);
    }
    private static void time(Player p) {
        // save player time
        Meta.set(p,Meta.LAST_TIME,String.valueOf(p.getPlayerTime()));
    }
    private static void emeralds(Player p) {
        // create atomic integer for emeralds
        AtomicInteger emeralds = new AtomicInteger();
        emeralds.set(0);
        // count emeralds in inventory
        for (ItemStack stack : p.getInventory().getContents()) {
            if (stack != null && stack.getType() == Material.EMERALD) {
                emeralds.getAndAdd(stack.getAmount());
            }
        }
        int others = Integer.parseInt(Meta.get(p,Meta.SAVINGS));
        
        // save emeralds
        Meta.set(p,Meta.EMERALDS_TOTAL,String.valueOf(emeralds.addAndGet(others)));
    }
    private static void facts(Player p) {
        // save all facts to database
        for (String fact : Facts.FACTS) {
            // save fact
            String data = PlaceholderAPI.setPlaceholders(p,"%typewriter_" + fact + "%");
            Meta.set(p,Meta.FACT + "_" + fact,data);
        }
    }
    private static void calculateDaily(Player p) {
        // get days
        int days = Integer.parseInt(Meta.get(p,Meta.DAYS));
        // get total values
        int score = Integer.parseInt(Meta.get(p,Meta.SCORE_TOTAL));
        int emeralds = Integer.parseInt(Meta.get(p,Meta.EMERALDS_TOTAL));
        int investments = Integer.parseInt(Meta.get(p,Meta.INVESTMENTS_TOTAL));
        int loan = Integer.parseInt(Meta.get(p,Meta.LOAN_TOTAL));
        // calculate daily and save to database
        Meta.set(p,Meta.SCORE_DAILY,String.valueOf(score / days));
        Meta.set(p,Meta.EMERALDS_DAILY,String.valueOf(emeralds / days));
        Meta.set(p,Meta.INVESTMENTS_DAILY,String.valueOf(investments / days));
        Meta.set(p,Meta.LOAN_DAILY,String.valueOf(loan / days));
    }
}
