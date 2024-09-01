package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.objects.Titles;
import org.bukkit.entity.Player;

public class Routines {
    public static void switchToNext(Player p) {
        // check which routine to start
        switch (Meta.get(p,Meta.ROUTINE)) {
            case "2":
                Routines.startEvening(p);
                break;
            case "3":
                // finish the day
                Game.finishDay(p);
                // if the player has played 10 days, end the game
                if (Meta.get(p,Meta.DAYS).equals("11")) {
                    // end game
                    Game.endGame(p);
                    return;
                }
                // show title
                p.showTitle(Titles.DAY(Meta.get(p,Meta.DAYS)));
                return;
        }
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> {
            // increment routine
            Meta.add(p,Meta.ROUTINE,1);
            // update database
            Meta.set(p,Meta.ACTIONS,"20");
            // update player action points
            p.setFoodLevel(20);
        }, Titles.TRANSITION_TELEPORT);
    }
    public static void startMorning(Player p) {
        Movers.toVillage(p);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> {
            Time.interpolate(p,0,9000);
            p.showTitle(Titles.ROUTINE(Meta.get(p,Meta.DAYS),"Ráno"));
        },Titles.TRANSITION_TELEPORT);
    }
    private static void startEvening(Player p) {
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> {
            Time.interpolate(p,9000,18000);
            p.showTitle(Titles.ROUTINE(Meta.get(p,Meta.DAYS),"Večer"));
        },Titles.TRANSITION_TELEPORT);
    }
}
