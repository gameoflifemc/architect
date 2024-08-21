package cc.architect.events.entity;

import cc.architect.minigames.travel.wraper.TravelRegistry;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Remove implements Listener {
    @EventHandler
    public void onRemove(EntityRemoveFromWorldEvent event) {
        TravelRegistry.entityRemove(event);
    }
}
