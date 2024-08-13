package cc.architect.leaderboards;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class InitLeaderBoards {
    public static final Location playerStats = new Location(Bukkit.getWorld("world"), -12.5, 129, 0.5,-90,0);
    public static final Location globalPlayerStats = new Location(Bukkit.getWorld("world"), 12.5, 129, 0.5,90,0);
    public static void init() {
        PlayerStatsBoard.createStatsLeaderBoard(playerStats);
        GlobalLeaderboard.createStatsLeaderBoard(globalPlayerStats);
    }
}
