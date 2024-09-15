package cc.architect.tasks.player;

import cc.architect.managers.Facts;
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
        Facts.saveAll();
        Bukkit.getOnlinePlayers().forEach(Autosave::autosave);
    }
    public static void autosave(Player p) {
        // don't autosave if player is in lobby
        if (p.getWorld().getName().equals("world")) {
            return;
        }
        location(p);
        emeralds(p);
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
        int savingsAdd = divideFloor(Integer.parseInt(Meta.get(p, Meta.SAVINGS))); // savings
        // save emeralds
        int loansRemove = divideCeil(Integer.parseInt(Meta.get(p,Meta.LOAN_SAFE)));//loans
        loansRemove += divideCeil(mapReader(Meta.get(p,Meta.LOAN_RISKY_MAP)));
        int investmentsAdd = mapReader(Meta.get(p,Meta.INVESTMENTS_MAP));
        int emeraldsF = emeralds.get();
        emeraldsF += savingsAdd;
        emeraldsF += investmentsAdd;
        emeraldsF -= loansRemove;
        emeraldsF = Math.max(0,emeraldsF);
        Meta.set(p,Meta.EMERALDS_TOTAL,String.valueOf(emeraldsF));
    }
    private static int mapReader(String map) {
        int total = 0;
        if(map != null) {
            String[] datas = map.split(";");
            if (!datas[0].isEmpty()) {
                for (String data : datas) {
                    String[] subdata = data.split(",");
                    int amount = Integer.parseInt(subdata[0]);
                    total += amount;
                }
            }
        }
        return total;
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
    public static int divideCeil(int i){
        return (int) Math.ceil((double) i / 10);
    }
    public static int divideFloor(int i){
        return (int) Math.floor((double) i / 10);
    }
}
