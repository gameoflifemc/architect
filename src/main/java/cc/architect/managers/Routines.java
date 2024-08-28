package cc.architect.managers;

import cc.architect.Architect;
import org.bukkit.entity.Player;

public class Routines {
    public static void switchToNext(Player p) {
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> {
            // check which routine to start
            switch (Meta.get(p,Meta.ROUTINE)) {
                case "1":
                    Routines.startEvening(p);
                    break;
                case "2":
                    // finish the day
                    Game.finishDay(p);
                    // if the player has played 10 days, end the game
                    if (Meta.get(p,Meta.DAYS).equals("10")) {
                        // end game
                        Game.endGame(p);
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
    public static void startMorning(Player p) {
        Movers.toVillage(p);
        Time.interpolate(p,0,9000);
    }
    private static void startEvening(Player p) {
        Movers.toVillage(p);
        Time.interpolate(p,9000,18000);
    }
}
