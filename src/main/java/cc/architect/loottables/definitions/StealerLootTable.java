package cc.architect.loottables.definitions;

import cc.architect.loottables.BaseLootTable;
import cc.architect.loottables.LootTableObject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StealerLootTable extends BaseLootTable {

    private List<LootTableObject> lootTableItems = List.of(
            new LootTableObject(100f, new ItemStack(Material.EMERALD, 9))
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
