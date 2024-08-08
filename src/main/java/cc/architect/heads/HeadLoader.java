package cc.architect.heads;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

// Loads all the heads needed for this plugin
public class HeadLoader {
    public static ItemStack CHEST;

    public static void load() {
        loadChest();
    }

    private static void loadChest() {
        CHEST = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) CHEST.getItemMeta();
        meta.setOwner("SunamyMC");

        CHEST.setItemMeta(meta);
    }
}
