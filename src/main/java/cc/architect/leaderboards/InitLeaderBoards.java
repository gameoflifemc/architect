package cc.architect.leaderboards;

import cc.architect.Architect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

public class InitLeaderBoards {
    public static final Location playerStats = new Location(Architect.WORLD,-11.5,128.5,0.5,-90,0);
    public static final Location globalPlayerStats1 = new Location(Architect.WORLD,12.5,129,0.5,90,0);
    public static final Location globalPlayerStats2 = new Location(Architect.VILLAGE,12.5,74,-87,90,0);
    public static BukkitTask villageBoard;

    public static void init() {
        PlayerStatsBoard.createStatsLeaderBoard(playerStats);
        GlobalLeaderboard.createStatsLeaderBoard(globalPlayerStats1);

        villageBoard = Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN, () -> {
            Bukkit.broadcastMessage("Village Leaderboard");
            globalPlayerStats2.getChunk().load();
            if(globalPlayerStats2.getChunk().isLoaded()) {
                try {
                    GlobalLeaderboard.createStatsLeaderBoard(globalPlayerStats2);
                    villageBoard.cancel();
                } catch (Exception ignored) {}

                //
            }
        },20,20);
    }
}
