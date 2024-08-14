package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.objects.Fonts;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.time.Duration;

public class Movers {
    private static final Location SPAWN = new Location(Bukkit.getWorld("village"),0.5,0,0.5,0,0);
    private static final Title TITLE1 = Title.title(Component.empty(),Component.text("0").font(Fonts.LOADING),
        Title.Times.times(Duration.ofMillis(500),Duration.ofMillis(1500),Duration.ZERO)
    );
    private static final Title TITLE2 = Title.title(Component.text("$#").font(Fonts.LOADING),Component.text("0").font(Fonts.LOADING),
        Title.Times.times(Duration.ZERO,Duration.ofMillis(7000),Duration.ZERO)
    );
    private static final Title TITLE3 = Title.title(Component.empty(),Component.text("0").font(Fonts.LOADING),
        Title.Times.times(Duration.ZERO,Duration.ofMillis(500),Duration.ofMillis(500))
    );
    public static void toWorld(Player p, String name) {
        World world = Bukkit.getWorld(name);
        if (world == null) {
            return;
        }
        p.teleport(world.getSpawnLocation().add(0.5,0,0.5));
    }
    public static void transition(Player p) {
        p.showTitle(TITLE1);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> {
            p.showTitle(TITLE2);
            p.teleport(SPAWN);
        },20);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> p.showTitle(TITLE3),120);
    }
    public static void toVillage(Player p) {
        transition(p);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> toWorld(p,"village"),100);
    }
    public static void toMine(Player p) {
        transition(p);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> toWorld(p,"mine"),100);
    }
    public static void toFarm(Player p) {
        transition(p);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> toWorld(p,"farm"),100);
    }
}
