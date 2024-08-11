package cc.architect.managers;

import cc.architect.Utilities;
import org.bukkit.entity.Player;

public class Routines {
    public static void enterGame(Player p) {
        Utilities.worldMove(p,"village");
    }
    public static void enterMine(Player p) {
        Utilities.worldMove(p,"mine");
    }
    public static void enterFarm(Player p) {
        Utilities.worldMove(p,"farm");
    }
    public static void enterDream(Player p) {
        Utilities.worldMove(p,"dream");
    }
    public static void endGame(Player p) {
        Utilities.worldMove(p,"world");
    }
}
