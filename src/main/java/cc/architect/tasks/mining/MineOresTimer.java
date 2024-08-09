package cc.architect.tasks.mining;

import cc.architect.minigames.mining.eventhandlers.BlockBreakHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public class MineOresTimer implements Runnable {

    @Override
    public void run() {
        Player[] players = BlockBreakHandler.minedOres.rowKeySet().toArray(new Player[0]);
        Location[] locations = BlockBreakHandler.minedOres.columnKeySet().toArray(new Location[0]);

        for (Player player : players) {
            for (Location location : locations) {
                if (BlockBreakHandler.minedOres.get(player,location) >= 1) {
                    Integer value = BlockBreakHandler.minedOres.get(player,location) -1;
                    BlockBreakHandler.minedOres.remove(player,location);
                    BlockBreakHandler.minedOres.put(player, location, value);
                }

                if (BlockBreakHandler.minedOres.get(player,location) == 0) {
                    BlockBreakHandler.minedOres.remove(player,location);
                }
            }
        }
    }
}
