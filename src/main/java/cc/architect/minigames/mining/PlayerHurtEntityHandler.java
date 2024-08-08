package cc.architect.minigames.mining;

import cc.architect.loottables.LootTableManager;
import cc.architect.loottables.definitions.StealerLootTable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerHurtEntityHandler {
    public static void handleEvent(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Ocelot) {
            if (event.getDamager() instanceof Player) {
                Player player = (Player) event.getDamager();
                player.getInventory().addItem(LootTableManager.roll(new StealerLootTable()));

                event.getEntity().getPassengers().forEach(Entity::remove);
                event.getEntity().remove();
            }
        }
    }
}
