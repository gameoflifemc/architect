package cc.architect.events.player;

import cc.architect.loottables.LootTableManager;
import cc.architect.loottables.definitions.MiningChestLootTable;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.SkullMeta;

public class InteractAtEntity implements Listener {
    @EventHandler
    public void onInteractAtEntity(org.bukkit.event.player.PlayerInteractAtEntityEvent e) {
        // Get if the entity is an ArmorStand
        if (e.getRightClicked() instanceof ArmorStand treasure) {
            // Get if the ArmorStand has a PlayerHead
            if (treasure.getEquipment().getHelmet().getItemMeta() instanceof SkullMeta skullMeta) {
                if (skullMeta.getCustomModelData() != 1) {
                    return;
                }
                // Get if the head has a texture of a chest
                Player p = e.getPlayer();
                p.getInventory().addItem(LootTableManager.roll(new MiningChestLootTable(p)));
                p.getWorld().spawnParticle(Particle.DUST,treasure.getLocation().add(0,2,0),4,new Particle.DustOptions(Color.WHITE,4));
                e.setCancelled(true);
                treasure.remove();
                MiningChestLootTable.miningChestsSpawned--;
            }
        }
    }
}
