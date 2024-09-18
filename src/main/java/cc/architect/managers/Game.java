package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.bonuses.DiamondBonus;
import cc.architect.commands.money.Loan;
import cc.architect.commands.money.Savings;
import cc.architect.commands.money.investments.InvestmentBasic;
import cc.architect.commands.money.investments.InvestmentRisky;
import cc.architect.objects.Compass;
import cc.architect.objects.HashMaps;
import cc.architect.objects.Titles;
import cc.architect.tasks.player.Autosave;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Game {
    public static final PotionEffect REGENERATION = new PotionEffect(PotionEffectType.REGENERATION,PotionEffect.INFINITE_DURATION,0,false,false);
    public static final float INVESTMENTS_PERCENT = 0.04F;
    public static final float INVESTMENTS_RISKY_MIN_PERCENT = -0.07F;
    public static final float INVESTMENTS_RISKY_MAX_PERCENT = 0.08F;
    public static final float LOAN_SPOR_INSTANT = 0.03F;
    public static final int SAVINGS_DIVIDER = 50;
    public static void beginDay(Player p) {
        if (!Meta.check(p,Meta.IS_IN_GAME)) {
            // prepare meta
            Meta.set(p,Meta.DAYS,"1");
            Meta.set(p,Meta.SAVINGS,"0");
            Meta.set(p,Meta.INVESTMENTS_TOTAL,"0");
            Meta.set(p,Meta.LOAN_TOTAL,"0");
            Meta.set(p,Meta.EMERALDS_TOTAL,"0");
            Meta.set(p,Meta.SCORE_TOTAL,"0");
            // investments
            Meta.set(p,Meta.INVESTMENTS_MAP,"");
            Meta.set(p,Meta.INVESTMENTS_MAP_RISKY,"");
            // risky loan
            Meta.set(p,Meta.LOAN_RISKY_MAP,"");
            Meta.set(p,Meta.LOAN_RISKY_COUNTER,"10");
            // safe loan
            Meta.set(p,Meta.LOAN_SAFE,"0");
            Meta.set(p,Meta.LOAN_SAFE_HAD_LOAN,"false");
            // is in game indicator
            Meta.set(p,Meta.IS_IN_GAME, "true");
        }
        // prepare meta
        Meta.set(p,Meta.LOAN_SAFE_HAD_LOAN,"false");
        Meta.set(p,Meta.ROUTINE,"1");
        Meta.set(p,Meta.ACTIONS,"20");
        p.setFoodLevel(20);
        // enter game
        Game.enterGame(p);
        // simulate interest
        Savings.handeSavingsAdder(p);
        //investments
        InvestmentBasic.handleInvestmentsAdder(p);
        InvestmentRisky.handleRiskyInvestmentsAdder(p);
        // loans
        Loan.handleLoanSporAdd(p);
        Loan.handleLoanLichvarAdd(p);
        // move to first routine
        Routines.startMorning(p);
    }
    public static void resumeDay(Player p) {
        // enter game
        Game.enterGame(p);
        // synchronize action points
        if (Meta.get(p,Meta.ACTIONS) != null) {
            p.setFoodLevel(Integer.parseInt(Meta.get(p,Meta.ACTIONS)));
        } else {
            p.setFoodLevel(20);
            Meta.set(p,Meta.ACTIONS,"20");
        }
        if (Meta.get(p,Meta.LAST_LOCATION) == null) {
            // teleport to village
            Movers.toVillage(p);
        } else {
            // teleport to last location
            Movers.toLastLocation(p);
        }
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> {
            // synchronize time
            switch (Meta.get(p,Meta.ROUTINE)) {
                case "1":
                    Time.set(p,6000);
                    break;
                case "2":
                    Time.set(p,18000);
                    break;
                default:
                    Time.set(p,6000);
                    Meta.set(p,Meta.ROUTINE,"1");
                    break;
            }
        },Titles.TRANSITION_TELEPORT);
    }
    public static void finishDay(Player p) {
        // calculate game information
        Autosave.autosave(p);
        Facts.saveOne(p);
        // remove everything concerning the given day, but keep stuff concerning the whole game
        Meta.clear(p,Meta.LAST_LOCATION);
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
        Meta.clear(p,Meta.INVESTMENTS_MAP_RISKY);
        Meta.clear(p,Meta.LOAN_SAFE_HAD_LOAN);
        Meta.clear(p,Meta.LOAN_SAFE);
        Meta.clear(p,Meta.LOAN_RISKY_MAP);
        Meta.clear(p,Meta.LOAN_RISKY_COUNTER);
        Meta.clear(p,Meta.IS_IN_GAME);
        // show title
        p.sendMessage(Component.text("Hra úspěšně dokončena. Gratulujeme! Skóre a další statistiky byly zapsány do leaderboardu."));
    }
    private static void enterGame(Player p) {
        Game.exitLobby(p);
        // synchronize facts
        Facts.restore(p);
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
        p.addPotionEffect(Game.REGENERATION);
    }
}
