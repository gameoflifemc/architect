package cc.architect.events.entity;

import cc.architect.Architect;
import cc.architect.loottables.LootTableManager;
import cc.architect.loottables.definitions.StealerLootTable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DamageByEntity implements Listener {
    @EventHandler
    public void onDamageByEntity(org.bukkit.event.entity.EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        if (!(damager instanceof Player p)) {
            return;
        }
        if (damager.getWorld() != Architect.MINE) {
            return;
        }
        Entity entity = e.getEntity();
        if (!(entity instanceof Ocelot)) {
            return;
        }
        p.getInventory().addItem(LootTableManager.roll(new StealerLootTable(p)));
        entity.getPassengers().forEach(Entity::remove);
        entity.remove();
    }
}
