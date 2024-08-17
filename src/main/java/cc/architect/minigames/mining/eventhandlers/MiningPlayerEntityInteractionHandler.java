package cc.architect.minigames.mining.eventhandlers;

import cc.architect.loottables.LootTableManager;
import cc.architect.loottables.definitions.MiningChestLootTable;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;

public class MiningPlayerEntityInteractionHandler {
    public static void handleEvent(PlayerInteractAtEntityEvent event) {

        // Get if the entity is an ArmorStand
        if (event.getRightClicked() instanceof ArmorStand treasure) {
            
            // Get if the ArmorStand has a PlayerHead
            if (treasure.getEquipment().getHelmet().getItemMeta() instanceof SkullMeta skullMeta) {
                OfflinePlayer owner = skullMeta.getOwningPlayer();
                if (owner == null) {
                    return;
                }
                // Get if the head has a texture of a chest
                if (Objects.equals(owner.getName(),"SunamyMC")) {
                    event.getPlayer().getInventory().addItem(LootTableManager.roll(new MiningChestLootTable(event.getPlayer())));

                    Particle particle = Particle.DUST;
                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 4);
                    event.getPlayer().getWorld().spawnParticle(particle, treasure.getLocation().add(0,2,0), 4, dustOptions);

                    event.setCancelled(true);
                    treasure.remove();
                    MiningChestLootTable.miningChestsSpawned--;
                }
            }
        }
    }
}
