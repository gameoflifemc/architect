package cc.architect;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.DataInputStream;
import java.io.IOException;

public class Utilities {
    /**
     * Give a player a carved pumpkin overlay.
     * @param p Player to give overlay.
     */
    public static void giveOverlay(Player p) {
        // prepare helmet item
        ItemStack item = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta meta = item.getItemMeta();
        meta.setHideTooltip(true);
        meta.setCustomModelData(1);
        item.setItemMeta(meta);
        // set helmet item
        p.getInventory().setHelmet(item);
    }
    /**
     * Add negative spaces in between a string.
     * @param text Text to add negative spaces to.
     * @return Text with negative spaces.
     */
    public static String addNegativeSpaces(String text) {
        // prepare new string
        StringBuilder sb = new StringBuilder();
        // remove spaces
        for (int i = 0; i < text.length(); i++) {
            sb.append(text.charAt(i)).append("@");
        }
        return sb.toString();
    }
    public static String readUTF(DataInputStream msgin) {
        try {
            return msgin.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
