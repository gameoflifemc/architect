package cc.architect.managers;

import cc.architect.Architect;
import org.bukkit.entity.Player;

public class Routines {
    public static void switchToNext(Player p) {
        Movers.showTransition(p);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> {
            // check which routine to start
            switch (Meta.get(p,Meta.ROUTINE)) {
                case "0":
                    Routines.startMorning(p);
                    break;
                case "1":
                    Routines.startEvening(p);
                    break;
                case "2":
                    // finish the day
                    Routines.finishDay(p);
                    // if the player has played 10 days, end the game
                    if (Meta.get(p,Meta.DAYS).equals("10")) {
                        // end game
                        Game.end(p);
                    }
                    break;
            }
            // increment routine
            Meta.add(p,Meta.ROUTINE,1);
            // update database
            Meta.set(p,Meta.ACTIONS,"20");
            // update player action points
            p.setFoodLevel(20);
        },100);
    }
    private static void startMorning(Player p) {
        Movers.toVillage(p);
        Time.setAbsolute(p,0);
        Time.interpolate(p,6000);
    }
    private static void startEvening(Player p) {
        Movers.toVillage(p);
    }
    public static void finishDay(Player p) {
        // remove everything concerning the given day, but keep stuff concerning the whole game
        Meta.clear(p,Meta.LAST_LOCATION);
        Meta.clear(p,Meta.ROUTINE);
        Meta.clear(p,Meta.ACTIONS);
        Movers.toSpawn(p);
        Meta.add(p,Meta.DAYS,1);
        // exit game
        Game.exit(p);
        // prepare player for next day
        Game.enterLobby(p);
    }
}
