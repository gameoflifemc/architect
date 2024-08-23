package cc.architect.heads;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class HeadLoader {
    public static ItemStack CHEST;
    public static void load() {
        CHEST = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) CHEST.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer("SunamyMC"));
        CHEST.setItemMeta(meta);
    }
}
