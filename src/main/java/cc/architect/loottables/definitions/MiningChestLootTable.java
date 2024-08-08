package cc.architect.loottables.definitions;

import cc.architect.loottables.BaseLootTable;
import cc.architect.loottables.LootTableObject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MiningChestLootTable extends BaseLootTable {

    public static int miningChestsSpawned = 0;

    private List<LootTableObject> lootTableItems = List.of(
            new LootTableObject(20, new ItemStack(Material.EMERALD, 7)),
            new LootTableObject(10, new ItemStack(Material.EMERALD, 10)),
            new LootTableObject(30, new ItemStack(Material.EMERALD, 5)),
            new LootTableObject(40, new ItemStack(Material.EMERALD, 4))
    );

    @Override
    public float[] getChances() {
        float[] chances = new float[lootTableItems.size()];

        for (int i = 0; i < lootTableItems.size(); i++) {
            chances[i] = lootTableItems.get(i).chance();
        }

        return chances;
    }

    @Override
    public ItemStack getItemStack(int rolledItem) {
        return lootTableItems.get(rolledItem).item();
    }
}
