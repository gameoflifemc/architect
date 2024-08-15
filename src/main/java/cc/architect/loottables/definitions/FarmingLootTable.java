package cc.architect.loottables.definitions;

import cc.architect.bonuses.DiamondBonus;
import cc.architect.loottables.BaseLootTable;
import cc.architect.loottables.LootTableObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FarmingLootTable extends BaseLootTable {
    private final List<LootTableObject> lootTableItems = List.of(
            new LootTableObject(5, new ItemStack(Material.EMERALD, 10)),
            new LootTableObject(42, new ItemStack(Material.EMERALD, 5)),
            new LootTableObject(53, new ItemStack(Material.EMERALD, 4))
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
