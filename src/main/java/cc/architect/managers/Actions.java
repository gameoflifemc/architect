package cc.architect.managers;

import cc.architect.objects.Fonts;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Actions {
    public static final HashMap<Player, Integer> actionPoints = new HashMap<>();
    public static final HashMap<Player, Boolean> canSeePoints = new HashMap<>();
    public static final int MAX_ACTIONS_VALUE = 10;
    public static final String META = "actions";
    private static final String MAX_ACTIONS = String.valueOf(MAX_ACTIONS_VALUE);
    private static final String READY = "1.";
    private static final String EMPTY = "0.";
    private static final String END = "=";
    public static void removePoint(Player p) {
        Meta.add(p,META,-1);
        actionPoints.put(p, actionPoints.get(p) - 1);
        showPoints(p);
        display(p);
        if (actionPoints.get(p) == 0) {
            Routines.next(p);
        }
    }
    public static void resetPoints(Player p) {
        Meta.set(p,META,MAX_ACTIONS);
        actionPoints.put(p, MAX_ACTIONS_VALUE);
        showPoints(p);
    }
    public static void hidePoints(Player p) {
        canSeePoints.put(p, false);
    }
    public static void showPoints(Player p) {
        canSeePoints.put(p, true);
    }
    public static void syncPoints(Player p) {
        actionPoints.put(p, Integer.parseInt(Meta.get(p,META)));
        showPoints(p);
    }
    public static void display(Player p) {
        int points = Actions.actionPoints.getOrDefault(p,0);
        String text = READY.repeat(points) + EMPTY.repeat(Actions.MAX_ACTIONS_VALUE - points) + END;
        p.sendActionBar(Component.text(text).font(Fonts.ACTION));
    }
}
