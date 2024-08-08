package cc.architect.loottables;

import org.bukkit.inventory.ItemStack;

public abstract class BaseLootTable {
    public abstract float[] getChances();
    public abstract ItemStack getItemStack(int rolledItem);
}

