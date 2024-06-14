package cc.architect;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utilities {
    public static void givePlayerDialogOverlay(Player p) {
        // prepare helmet item
        ItemStack item = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta meta = item.getItemMeta();
        meta.setHideTooltip(true);
        meta.setCustomModelData(1);
        item.setItemMeta(meta);
        // set helmet item
        p.getInventory().setHelmet(item);
    }
}
