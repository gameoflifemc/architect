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
    public static void toTravel(Player p, Location location) {
        Movers.showTransition(p);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> p.teleport(location),Titles.TRANSITION_TELEPORT);
    }
    public static void toLastLocation(Player p) {
        Movers.showTransition(p);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> {
            String[] data = Meta.get(p,Meta.LAST_LOCATION).split(",");
            World world = Bukkit.getWorld(data[0]);
            if (world == null) {
                return;
            }
            p.teleport(new Location(world,Double.parseDouble(data[1]),Double.parseDouble(data[2]),Double.parseDouble(data[3]),Float.parseFloat(data[4]),Float.parseFloat(data[5])));
        },Titles.TRANSITION_TELEPORT);
    }
}
