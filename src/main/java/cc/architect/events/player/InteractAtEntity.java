package cc.architect.events.player;

import cc.architect.loottables.LootTableManager;
import cc.architect.loottables.definitions.MiningChestLootTable;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;

public class InteractAtEntity implements Listener {
    @EventHandler
    public void onInteractAtEntity(org.bukkit.event.player.PlayerInteractAtEntityEvent e) {
        // Get if the entity is an ArmorStand
        if (e.getRightClicked() instanceof ArmorStand treasure) {
            // Get if the ArmorStand has a PlayerHead
            if (treasure.getEquipment().getHelmet().getItemMeta() instanceof SkullMeta skullMeta) {
                OfflinePlayer owner = skullMeta.getOwningPlayer();
                if (owner == null) {
                    return;
                }
                // Get if the head has a texture of a chest
                if (Objects.equals(owner.getName(),"SunamyMC")) {
                    Player p = e.getPlayer();
                    p.getInventory().addItem(LootTableManager.roll(new MiningChestLootTable(p)));
                    Particle particle = Particle.DUST;
                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 4);
                    p.getWorld().spawnParticle(particle, treasure.getLocation().add(0, 2, 0), 4, dustOptions);
                    e.setCancelled(true);
                    treasure.remove();
                    MiningChestLootTable.miningChestsSpawned--;
                }
            }
        }
    }
}
