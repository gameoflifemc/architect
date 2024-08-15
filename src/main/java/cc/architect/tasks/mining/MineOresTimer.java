package cc.architect.tasks.mining;

import cc.architect.minigames.mining.eventhandlers.MiningBlockBreakHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MineOresTimer implements Runnable {
    @Override
    public void run() {
        Player[] players = MiningBlockBreakHandler.minedOres.rowKeySet().toArray(new Player[0]);
        Location[] locations = MiningBlockBreakHandler.minedOres.columnKeySet().toArray(new Location[0]);

        for (Player player : players) {
            for (Location location : locations) {
                if (MiningBlockBreakHandler.minedOres.get(player,location) >= 1) {
                    Integer value = MiningBlockBreakHandler.minedOres.get(player,location) -1;
                    MiningBlockBreakHandler.minedOres.remove(player,location);
                    MiningBlockBreakHandler.minedOres.put(player, location, value);
                }

                if (MiningBlockBreakHandler.minedOres.get(player,location) == 0) {
                    MiningBlockBreakHandler.minedOres.remove(player,location);
                }
            }
        }
    }
}
