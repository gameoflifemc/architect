package cc.architect.tasks;

import cc.architect.loottables.LootTableManager;
import cc.architect.loottables.definitions.ReplenishBedrockLootTable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ReplenishBedrock implements Runnable {

    private List<Location> minedBlocks = new ArrayList<>();

    @Override
    public void run() {
        for(Location minedBlock : minedBlocks) {
            Material replace = LootTableManager.roll(new ReplenishBedrockLootTable()).getType();
            minedBlock.getBlock().setType(replace);
        }
    }

    public void addBedrock(Location location) {
        minedBlocks.add(location);
    }
}
