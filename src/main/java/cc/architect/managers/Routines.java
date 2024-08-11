package cc.architect.managers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Routines {
    public static void enterGame(Player p) {
        worldMove(p,"village");
        Actions.resetPoints(p);
    }
    public static void enterMine(Player p) {
        worldMove(p,"mine");
        Actions.resetPoints(p);
    }
    public static void enterFarm(Player p) {
        worldMove(p,"farm");
        Actions.resetPoints(p);
    }
    public static void enterDream(Player p) {
        worldMove(p,"dream");
        Actions.resetPoints(p);
    }
    public static void endGame(Player p) {
        worldMove(p,"world");
        Actions.resetPoints(p);
    }
    private static void worldMove(Player player, String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return;
        }
        player.teleport(world.getSpawnLocation());
    }
}
