package cc.architect.leaderboards;

import cc.architect.Architect;
import org.bukkit.Location;

public class InitLeaderBoards {
    public static final Location playerStats = new Location(Architect.WORLD,-11.5,128.5,0.5,-90,0);
    public static final Location globalPlayerStats = new Location(Architect.WORLD,12.5,129,0.5,90,0);
    public static void init() {
        PlayerStatsBoard.createStatsLeaderBoard(playerStats);
        GlobalLeaderboard.createStatsLeaderBoard(globalPlayerStats);
    }
}
