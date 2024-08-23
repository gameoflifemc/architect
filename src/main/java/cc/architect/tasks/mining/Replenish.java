package cc.architect.tasks.mining;

import cc.architect.loottables.LootTableManager;
import cc.architect.loottables.definitions.ReplenishBedrockLootTable;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;

/**
* Replenish bedrock in the mining minigame
**/
public class Replenish implements Runnable {
    private final HashMap<Location, Material> minedBlocks = new HashMap<>();
    @Override
    public void run() {
        for (Location minedBlock : minedBlocks.keySet()) {
            Material replace = LootTableManager.roll(new ReplenishBedrockLootTable()).getType();
            minedBlock.getBlock().setType(minedBlocks.get(minedBlock).equals(Material.BEDROCK) ? replace : minedBlocks.get(minedBlock));
        }
        minedBlocks.clear();
    }
    public void addBedrock(Location location, Material material) {
        minedBlocks.put(location, material);
    }
    public boolean hasLocation(Location location) {
        return minedBlocks.containsKey(location);
    }
}
