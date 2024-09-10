package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.bonuses.DiamondBonus;
import cc.architect.objects.Compass;
import cc.architect.objects.HashMaps;
import cc.architect.objects.Titles;
import cc.architect.tasks.player.Autosave;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Game {
    private static final PotionEffect REGENERATION = new PotionEffect(PotionEffectType.REGENERATION,PotionEffect.INFINITE_DURATION,0,false,false);
    private static final float INVESTMENTS_PERCENT = .04f;
    private static final int SAVINGS_DIVIDER = 50;
    public static final float LOAN_SPOR_INSTANT = .03f;
    public static final int LICH_SPOR_INSTANT = 10;
    public static void beginDay(Player p) {
        if (!Meta.check(p,Meta.DAYS)) {
            // prepare meta
            Meta.set(p,Meta.DAYS,"1");
            Meta.set(p,Meta.SAVINGS,"0");
            Meta.set(p,Meta.INVESTMENTS_TOTAL,"0");
            Meta.set(p,Meta.LOAN_TOTAL,"0");
            Meta.set(p,Meta.EMERALDS_TOTAL,"0");
            Meta.set(p,Meta.SCORE_TOTAL,"0");
            Meta.set(p,Meta.INVESTMENTS_MAP,"");

            Meta.set(p,Meta.LOAN_RISKY_MAP,"");
            Meta.set(p,Meta.LOAN_RISKY_COUNTER,"10");

            Meta.set(p,Meta.LOAN_SAFE,"0");
            Meta.set(p,Meta.LOAN_SAFE_HAD_LOAN,"false");
        }
        // prepare meta
        Meta.set(p,Meta.ROUTINE,"1");
        Meta.set(p,Meta.ACTIONS,"20");
        p.setFoodLevel(20);
        // enter game
        Game.enterGame(p);
        // simulate interest
        int savings = Integer.parseInt(Meta.get(p,Meta.SAVINGS));
        Meta.add(p,Meta.SAVINGS,savings / SAVINGS_DIVIDER);

        int loan_spor = Integer.parseInt(Meta.get(p,Meta.LOAN_SAFE));
        //Meta.add(p,Meta.INVESTMENTS_TOTAL, investments / INTEREST_DIVIDER);
        Meta.add(p,Meta.LOAN_SAFE, (int) (loan_spor * LOAN_SPOR_INSTANT));

        handleInvestments(p);
        handleLoanLich(p);

        // move to first routine
        Routines.startMorning(p);
    }
    public static void handleInvestments(Player p) {
        //investice
        StringBuilder invBuilder = new StringBuilder();
        String investmentsMap = Meta.get(p,Meta.INVESTMENTS_MAP);
        String[] investments = investmentsMap.split(";");
        if(investments[0].isEmpty()) return;

        for (String investment : investments) {
            String[] data = investment.split(",");
            int amount = Integer.parseInt(data[0]);
            int days = Math.max(Integer.parseInt(data[1]),0);

            int newAmount = amount + (int)(amount * INVESTMENTS_PERCENT);
            if(days==0){
                newAmount = amount;
            }
            invBuilder.append(newAmount).append(",").append(days).append(";");
        }

        Meta.set(p,Meta.INVESTMENTS_MAP,invBuilder.toString());
    }

    public static void handleLoanLich(Player p) {
        //investice
        StringBuilder loanBuilder = new StringBuilder();
        String loansMap = Meta.get(p,Meta.LOAN_RISKY_MAP);
        String[] loans = loansMap.split(";");
        if(loans[0].isEmpty()) return;

        for (String investment : loans) {
            String[] data = investment.split(",");
            int amount = Integer.parseInt(data[0]);
            int adder = Integer.parseInt(data[1]);

            int newAmount = amount + adder;
            loanBuilder.append(newAmount).append(",").append(adder).append(";");
        }

        Meta.set(p,Meta.LOAN_RISKY_MAP,loanBuilder.toString());
    }
    public static void resumeDay(Player p) {
        // enter game
        Game.enterGame(p);
        // synchronize action points
        if(Meta.get(p,Meta.ACTIONS)!=null) {
            p.setFoodLevel(Integer.parseInt(Meta.get(p, Meta.ACTIONS)));
        }else{
            p.setFoodLevel(20);
            Meta.set(p,Meta.ACTIONS,"20");
        }
        // show transition
        Movers.showTransition(p);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> {
            if(Meta.get(p, Meta.LAST_LOCATION)==null){
                p.teleport(Bukkit.getWorld("village").getSpawnLocation());
            }else {
                // teleport to last location
                String[] data = Meta.get(p, Meta.LAST_LOCATION).split(",");
                World world = Bukkit.getWorld(data[0]);
                if (world == null) {
                    return;
                }
                p.teleport(new Location(world, Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]), Float.parseFloat(data[4]), Float.parseFloat(data[5])));
            }
            // synchronize time
            switch (Meta.get(p,Meta.ROUTINE)) {
                case "1":
                    Time.interpolate(p,Long.parseLong(Meta.get(p,Meta.LAST_TIME)),9000);
                    break;
                case "2":
                    Time.interpolate(p,Long.parseLong(Meta.get(p,Meta.LAST_TIME)),18000);
                    break;
            }
        }, Titles.TRANSITION_TELEPORT);
    }
    public static void finishDay(Player p) {
        // calculate game information
        Autosave.autosave(p);
        // remove everything concerning the given day, but keep stuff concerning the whole game
        Meta.clear(p,Meta.LAST_LOCATION);
        Meta.clear(p,Meta.LAST_TIME);
        Meta.clear(p,Meta.ACTIONS);
        Meta.clear(p,Meta.ROUTINE);

        Meta.set(p,Meta.LOAN_SAFE_HAD_LOAN,"false");
        p.setFoodLevel(20);
        // move to spawn
        Movers.toSpawn(p);
        // increment days
        Meta.add(p,Meta.DAYS,1);
        // exit game
        Game.exitGame(p);
        // prepare player for next day
        Game.enterLobby(p);
    }
    public static void endGame(Player p) {
        // remove everything concerning the whole game
        Meta.clear(p,Meta.DAYS);
        Meta.clear(p,Meta.SAVINGS);
        Meta.clear(p,Meta.INVESTMENTS_TOTAL);
        Meta.clear(p,Meta.LOAN_TOTAL);
        Meta.clear(p,Meta.EMERALDS_TOTAL);
        Meta.clear(p,Meta.SCORE_TOTAL);
        Meta.clear(p,Meta.INVESTMENTS_MAP);
        Meta.clear(p,Meta.LOAN_SAFE_HAD_LOAN);
        Meta.clear(p,Meta.LOAN_SAFE);
        Meta.clear(p,Meta.LOAN_RISKY_MAP);
        Meta.clear(p,Meta.LOAN_RISKY_COUNTER);
        // show title
        p.sendMessage(Component.text("Hra úspěšně dokončena. Gratulujeme! Skóre a další statistiky byly zapsány do leaderboardu."));
    }
    private static void enterGame(Player p) {
        Game.exitLobby(p);
        // synchronize facts
        Facts.synchronize(p);
        // create compass
        if (!HashMaps.COMPASSES.containsKey(p)) {
            // create compass
            HashMaps.COMPASSES.put(p,new Compass(p));
        }
        // initialize bonus
        DiamondBonus.initPlayer(p);
    }
    public static void exitGame(Player p) {
        // remove all bossbars from player
        Compass compass = HashMaps.COMPASSES.get(p);
        if (compass != null) {
            compass.getDirections().removeViewer(p);
            compass.getLocations().removeViewer(p);
            compass.getBackground().removeViewer(p);
        }
        // delete compass
        HashMaps.COMPASSES.remove(p);
        HashMaps.WORLD_LOCATIONS.remove(p);
        HashMaps.ROTATION_LOCATIONS.remove(p);
        // remove regeneration
        p.removePotionEffect(PotionEffectType.REGENERATION);
    }
    public static void enterLobby(Player p) {
        // give player spyglass
        PlayerInventory inventory = p.getInventory();
        HashMaps.REPLACEMENT_ITEM.put(p,inventory.getItemInMainHand());
        inventory.setItemInMainHand(Items.SPYGLASS);
        // add player to action bar
        HashMaps.ACTION_BAR.add(p);
    }
    public static void exitLobby(Player p) {
        // remove spyglass
        Items.removeSpyglass(p);
        // remove player from spyglass used
        HashMaps.SPYGLASS_USED.remove(p);
        // remove player from action bar
        HashMaps.ACTION_BAR.remove(p);
        p.sendActionBar(Component.empty());
        // give regeneration
        p.addPotionEffect(REGENERATION);
    }
}
