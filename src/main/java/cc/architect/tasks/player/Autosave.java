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
        calculateHighest(p);
        calculateDaily(p);
    }
    private static void location(Player p) {
        // get last location
        Location loc = p.getLocation();
        // turn location into data
        String data = loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
        // save player location
        Meta.set(p,Meta.LAST_LOCATION,data);
    }
    private static void time(Player p) {
        // save player time
        Meta.set(p,Meta.LAST_TIME,String.valueOf(p.getPlayerTime()));
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
        int others = (int) Math.floor((double) Integer.parseInt(Meta.get(p, Meta.SAVINGS)) /10)
                - Integer.parseInt(Meta.get(p,Meta.LOAN_TOTAL));
        // save emeralds
        int remover = Integer.parseInt(Meta.get(p,Meta.LOAN_SPOR));

        String lichMap = Meta.get(p,Meta.LOAN_LICH_MAP);
        String[] loans = lichMap.split(";");

        if(!loans[0].isEmpty()) {
            for (String loan : loans) {
                String[] data = loan.split(",");
                int loanAmount = Integer.parseInt(data[0]);
                remover += loanAmount;
            }
        }

        String investmentsMap = Meta.get(p,Meta.INVESTMENTS_MAP);
        String[] investments = investmentsMap.split(";");

        int playerDay = Integer.parseInt(Meta.get(p,Meta.DAYS));

        for (String investment : investments) {
            String[] data = investment.split(",");
            int amount = Integer.parseInt(data[0]);
            int days = Math.max(Integer.parseInt(data[1]) - playerDay, 0);
            if(days == 0) {
                others += amount;
            }
        }

        int emeraldsF = Math.max((emeralds.get()-remover)+others,0);

        Meta.set(p,Meta.EMERALDS_TOTAL,String.valueOf(emeraldsF));
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
    public static void calculateHighest(Player p) {
        // calculate highest and save to database

        Meta.set(p,Meta.SCORE_HIGHEST,Meta.get(p,Meta.SCORE_TOTAL));
        Meta.set(p,Meta.EMERALDS_HIGHEST,Meta.get(p,Meta.EMERALDS_TOTAL));
        Meta.set(p,Meta.INVESTMENTS_HIGHEST,Meta.get(p,Meta.INVESTMENTS_TOTAL));
        Meta.set(p,Meta.LOAN_HIGHEST,Meta.get(p,Meta.LOAN_TOTAL));
    }
}
