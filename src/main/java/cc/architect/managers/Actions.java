package cc.architect.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class Actions {
    public static final HashMap<Player, Integer> pointsCache = new HashMap<>();
    public static final String META = "actions";
    public static void removePoint(Player p) {
        // remove from database
        Meta.add(p,META,-2);
        // get current value
        int current = Actions.pointsCache.get(p);
        // if player has no points, move to next routine
        if (current == 0) {
            Routines.next(p);
        } else {
            // calculate new value
            int next = current - 2;
            // update cache
            Actions.pointsCache.put(p, next);
            // update player
            p.setFoodLevel(next);
        }
    }
    public static void resetPoints(Player p) {
        // reset database
        Meta.set(p,META,"20");
        // update cache
        pointsCache.put(p, 20);
        // update player
        p.setFoodLevel(20);
    }
    public static void syncPoints(Player p) {
        pointsCache.put(p, Integer.parseInt(Meta.get(p,META)));
    }
}
