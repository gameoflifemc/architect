package cc.architect.managers;

import cc.architect.objects.Colors;
import cc.architect.objects.HashMaps;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Items {
    public static ItemStack CHEST;
    public static ItemStack SPYGLASS;
    public static void loadAll() {
        // load chest
        ItemStack chest = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta chestMeta = (SkullMeta) chest.getItemMeta();
        chestMeta.setOwningPlayer(Bukkit.getOfflinePlayer("SunamyMC"));
        chestMeta.setCustomModelData(1);
        chest.setItemMeta(chestMeta);
        CHEST = chest;
        // load spyglass
        ItemStack spyglass = new ItemStack(Material.SPYGLASS);
        ItemMeta spyglassMeta = spyglass.getItemMeta();
        spyglassMeta.itemName(Component.text("VR Headset").color(Colors.BASE));
        spyglassMeta.setCustomModelData(1);
        spyglassMeta.setHideTooltip(true);
        spyglass.setItemMeta(spyglassMeta);
        SPYGLASS = spyglass;
    }
    public static void removeSpyglass(Player p) {
        PlayerInventory inv = p.getInventory();
        int index = inv.first(SPYGLASS);
        if (index == -1) {
            return;
        }
        inv.clear(index);
        inv.setItem(index,HashMaps.REPLACEMENT_ITEM.get(p));
        HashMaps.REPLACEMENT_ITEM.remove(p);
        // just in case
        inv.removeItemAnySlot(SPYGLASS);
    }
}
