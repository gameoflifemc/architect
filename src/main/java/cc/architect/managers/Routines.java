package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.events.player.SpawnLocation;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Routines {
    public static final HashMap<Player, Integer> current = new HashMap<>();
    public static void next(Player p) {
        Movers.transition(p);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> transport(p),100);
    }
    private static void transport(Player p) {
        int current = Routines.current.getOrDefault(p,0);
        switch (current) {
            case 0:
                morning(p);
                break;
            case 1:
                afternoon(p);
                break;
            case 2:
                evening(p);
                break;
            case 3:
                night(p);
                break;
            case 4:
                finishDay(p);
                return;
        }
        Routines.current.put(p, current + 1);
        Actions.resetPoints(p);
    }
    private static void morning(Player p) {
        Movers.toWorld(p,"village");
    }
    private static void afternoon(Player p) {
        Movers.toWorld(p,"village");
    }
    private static void evening(Player p) {
        Movers.toWorld(p,"village");
    }
    private static void night(Player p) {
        Movers.toWorld(p,"dream");
    }
    public static void finishDay(Player p) {
        // remove everything concerning the given day, but keep stuff concerning the whole game
        Movers.toWorld(p,"world");
        Meta.clear(p,SpawnLocation.META);
        Meta.clear(p,Actions.META);
        Actions.pointsCache.remove(p);
        Routines.current.remove(p);
    }
}
