package cc.architect.tasks.mining;

import cc.architect.minigames.mining.eventhandlers.MiningBlockBreakHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MineOresTimer implements Runnable {
    @Override
    public void run() {
        Player[] players = MiningBlockBreakHandler.minedOres.rowKeySet().toArray(new Player[0]);
        Location[] locations = MiningBlockBreakHandler.minedOres.columnKeySet().toArray(new Location[0]);
        for (Player p : players) {
            if (p == null) {
                continue;
            }
            for (Location location : locations) {
                if (location == null) {
                    continue;
                }
                int ores = MiningBlockBreakHandler.minedOres.get(p,location);
                if (ores >= 1) {
                    MiningBlockBreakHandler.minedOres.remove(p,location);
                    MiningBlockBreakHandler.minedOres.put(p,location,ores - 1);
                }
                if (ores == 0) {
                    MiningBlockBreakHandler.minedOres.remove(p,location);
                }
            }
        }
    }
}
