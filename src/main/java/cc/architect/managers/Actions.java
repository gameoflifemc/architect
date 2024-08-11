package cc.architect.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class Actions {
    public static HashMap<Player, Boolean> canSeePoints = new HashMap<>();
    public static void removePoint(Player p) {
        //if (!points.containsKey(p)) {
        //    return;
        //}
        //points.computeIfPresent(p,(k, currentPoints) -> currentPoints - 1);
    }
    public static void resetPoints(Player p) {
        //points.put(p, 10);
        showPoints(p);
    }
    public static void hidePoints(Player p) {
        canSeePoints.put(p,false);
    }
    public static void showPoints(Player p) {
        canSeePoints.put(p,true);
    }
}
