package cc.architect.events.entity;

import cc.architect.minigames.travel.wrapper.TravelRegistry;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RemoveFromWorld implements Listener {
    @EventHandler
    public void onRemoveFromWorld(EntityRemoveFromWorldEvent event) {
        TravelRegistry.entityRemove(event);
    }
}
