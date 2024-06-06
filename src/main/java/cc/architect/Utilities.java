package cc.architect;

import cc.architect.objects.TargetRotations;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utilities {
    public static TargetRotations getRotationToEntity(Location origin, Location target) {
        // prepare variables
        TargetRotations result = new TargetRotations();
        // get relative positions
        double dx = target.getX() - origin.getX();
        double dy = target.getY() - origin.getY();
        double dz = target.getZ() - origin.getZ();
        // set yaw
        if (dx != 0) {
            // set yaw start value based on dx
            if (dx < 0) {
                result.setYaw((float) (1.5 * Math.PI));
            } else {
                result.setYaw((float) (0.5 * Math.PI));
            }
            result.setYaw(result.getYaw() - (float) Math.atan(dz / dx));
        } else if (dz < 0) {
            result.setYaw((float) Math.PI);
        }
        // get distance from dx/dz
        double dxz = Math.sqrt(dx * dx + dz * dz);
        // set pitch
        result.setPitch((float) -Math.atan(dy / dxz));
        // set values, convert to degrees
        result.setYaw(-result.getYaw() * 180f / (float) Math.PI);
        result.setPitch(result.getPitch() * 180f / (float) Math.PI);
        return result;
    }
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
