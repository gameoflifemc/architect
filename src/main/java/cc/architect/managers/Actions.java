package cc.architect.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class Actions {
    public static HashMap<Player, Boolean> canSeePoints = new HashMap<>();
    public static void removePoint(Player p) {
        Meta.addValue(p,"actions",-1);
    }
    public static void resetPoints(Player p) {
        Meta.setValue(p,"actions","0");
        showPoints(p);
    }
    public static void hidePoints(Player p) {
        canSeePoints.put(p,false);
    }
    public static void showPoints(Player p) {
        canSeePoints.put(p,true);
    }
}
