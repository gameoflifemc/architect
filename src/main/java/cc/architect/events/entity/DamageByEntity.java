package cc.architect.events.entity;

import cc.architect.Architect;
import cc.architect.loottables.LootTableManager;
import cc.architect.loottables.definitions.StealerLootTable;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DamageByEntity implements Listener {
    @EventHandler
    public void onDamageByEntity(org.bukkit.event.entity.EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player p)) {
            return;
        }
        Entity entity = e.getEntity();
        if (entity instanceof Pig || entity instanceof Cow || entity instanceof Sheep || entity instanceof Chicken) {
            e.setCancelled(true);
            return;
        }
        if (p.getWorld() == Architect.MINE && entity instanceof Ocelot) {
            p.getInventory().addItem(LootTableManager.roll(new StealerLootTable(p)));
            entity.getPassengers().forEach(Entity::remove);
            entity.remove();
        }
    }
}
