package cc.architect.events.entity;

import cc.architect.minigames.mining.PlayerHurtEntityHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerHurtEntity implements Listener {
    @EventHandler
    public void playerHurtEntity(EntityDamageByEntityEvent event) {
         PlayerHurtEntityHandler.handleEvent(event);
    }
}
