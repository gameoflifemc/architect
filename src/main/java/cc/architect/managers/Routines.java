package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.events.player.SpawnLocation;
import cc.architect.objects.Fonts;
import cc.architect.objects.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.HashMap;

import static cc.architect.managers.Actions.actionPoints;
import static cc.architect.managers.Actions.canSeePoints;

public class Routines {
    private static final HashMap<Player, Integer> current = new HashMap<>();
    private static final Title TITLE1 = Title.title(Component.empty(),Component.text("0").font(Fonts.LOADING),
        Title.Times.times(Duration.ofMillis(500),Duration.ofMillis(1500),Duration.ZERO)
    );
    private static final Title TITLE2 = Title.title(Component.text("$#").font(Fonts.LOADING),Component.text("0").font(Fonts.LOADING),
        Title.Times.times(Duration.ZERO,Duration.ofMillis(9000),Duration.ZERO)
    );
    private static final Title TITLE3 = Title.title(Component.empty(),Component.text("0").font(Fonts.LOADING),
        Title.Times.times(Duration.ZERO,Duration.ofMillis(500),Duration.ofMillis(500))
    );
    public static void next(Player p) {
        p.showTitle(TITLE1);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> p.showTitle(TITLE2),20);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> sendToNext(p),100);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> p.showTitle(TITLE3),180);
    }
    private static void sendToNext(Player p) {
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
                endGame(p);
                Routines.current.remove(p);
                return;
        }
        Routines.current.put(p, current + 1);
    }
    private static void morning(Player p) {
        worldMove(p,"village");
        Actions.resetPoints(p);
    }
    private static void afternoon(Player p) {
        worldMove(p,"village");
        Actions.resetPoints(p);
    }
    private static void evening(Player p) {
        worldMove(p,"village");
        Actions.resetPoints(p);
    }
    private static void night(Player p) {
        worldMove(p,"dream");
        Actions.resetPoints(p);
    }
    public static void toMine(Player p) {
        worldMove(p,"mine");
        Actions.resetPoints(p);
    }
    public static void toFarm(Player p) {
        worldMove(p,"farm");
        Actions.resetPoints(p);
    }
    public static void startGame(Player p) {
        current.put(p,0);
        next(p);
    }
    public static void continueGame(Player p) {
        // welcome back message
        p.sendMessage(Messages.WELCOME_BACK);
        // sync action points
        Actions.syncPoints(p);
    }
    public static void endGame(Player p) {
        worldMove(p,"world");
        Meta.clear(p,SpawnLocation.META);
        Meta.clear(p,Actions.META);
        actionPoints.remove(p);
        canSeePoints.remove(p);
    }
    private static void worldMove(Player player, String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return;
        }
        player.teleport(world.getSpawnLocation());
    }
}
