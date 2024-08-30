package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.objects.Titles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Movers {
    private static final Location LOADING_AREA = new Location(Bukkit.getWorld("village"),0.5,0,0.5,0,0);
    public static void toWorld(Player p, String name) {
        World world = Bukkit.getWorld(name);
        if (world == null) {
            return;
        }
        p.teleport(world.getSpawnLocation().add(0.5,0,0.5));
    }
    public static void showTransition(Player p) {
        p.showTitle(Titles.TRANSITION1);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> {
            p.showTitle(Titles.TRANSITION2);
            p.teleport(LOADING_AREA);
            Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> p.showTitle(Titles.TRANSITION3),60);
        },20);
    }
    public static void toSpawn(Player p) {
        Movers.showTransition(p);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> toWorld(p,"world"),Titles.TRANSITION_TELEPORT);
    }
    public static void toVillage(Player p) {
        Movers.showTransition(p);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> toWorld(p,"village"),Titles.TRANSITION_TELEPORT);
    }
    public static void toMine(Player p) {
        Movers.showTransition(p);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> toWorld(p,"mine"),Titles.TRANSITION_TELEPORT);
    }
    public static void toFarm(Player p) {
        Movers.showTransition(p);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> toWorld(p,"farm"),Titles.TRANSITION_TELEPORT);
    }

    public static void toTravel(Player p, Location loc) {
        Movers.showTransition(p);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> p.teleport(loc),100);
    }
}
