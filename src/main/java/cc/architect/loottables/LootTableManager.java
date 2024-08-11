package cc.architect.loottables;

import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Custom Loot Table System
**/
public class LootTableManager {
    /**
     * Get random item from a predefined loot table
     * @param lootTable An object that extends BaseLootTable
     * @return Random ItemStack based on the chances defined in input object
     **/
    public static ItemStack roll(BaseLootTable lootTable) {
        float[] chances = lootTable.getChances();
        if(!verify(chances)) {
            throw new IllegalStateException("Loot table " + lootTable.getClass().getName() + " is not configured properly.");
        }

        float[] modifiedChances = modifyChancesForRoll(chances);

        Random random = new Random();
        float randomNumber = random.nextFloat() * 100;

        int targetIndex = 0;
        for (int i = 0; i < modifiedChances.length; i++) {
            float chance = modifiedChances[i];
            if (randomNumber <= chance) {
                targetIndex = i;
                break;
            }
        }

        return lootTable.getItemStack(targetIndex);
    }

    // verify that chances add up to 100%
    private static boolean verify(float[] chances) {
        float total = 0;
        for (float value : chances) {
            total += value;
        }
        return total == 100f;
    }

    // modify chances before roll
    private static float[] modifyChancesForRoll(float[] chances) {
        float[] out = new float[chances.length];
        float addition = 0;
        for (int i = 0; i < chances.length; i++) {
            out[i] = addition + chances[i];
            addition += chances[i];
        }

        return out;
    }
}
