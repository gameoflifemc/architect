package cc.architect.events.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Damage implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        // get cause of damage
        DamageCause cause = e.getCause();
        // disable wither damage
        if (cause.equals(DamageCause.WITHER) || cause.equals(DamageCause.STARVATION)) {
            e.setCancelled(true);
        }
    }
}
