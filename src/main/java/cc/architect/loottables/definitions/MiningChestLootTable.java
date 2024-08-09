package cc.architect.loottables.definitions;

import cc.architect.bonuses.DiamondBonus;
import cc.architect.loottables.BaseLootTable;
import cc.architect.loottables.LootTableObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MiningChestLootTable extends BaseLootTable {

    public static int miningChestsSpawned = 0;
    public Player player;
    private final List<LootTableObject> lootTableItems;

    public MiningChestLootTable(Player player) {
        this.player = player;
        int bonus = (int) Math.ceil(DiamondBonus.bonuses.get(player));
        lootTableItems = List.of(
                new LootTableObject(20, new ItemStack(Material.EMERALD, 7 * bonus)),
                new LootTableObject(10, new ItemStack(Material.EMERALD, 10 * bonus)),
                new LootTableObject(30, new ItemStack(Material.EMERALD, 5 * bonus)),
                new LootTableObject(40, new ItemStack(Material.EMERALD, 4 * bonus))
        );
    }

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
