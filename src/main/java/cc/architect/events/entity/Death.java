package cc.architect.events.entity;

import cc.architect.minigames.travel.wrapper.TravelRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class Death implements Listener {
    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        TravelRegistry.entityDeath(event);
    }
}
