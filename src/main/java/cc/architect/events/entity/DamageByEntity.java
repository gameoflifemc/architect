package cc.architect.events.entity;

import cc.architect.minigames.mining.eventhandlers.PlayerHurtEntityHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageByEntity implements Listener {
    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
         PlayerHurtEntityHandler.handleEvent(e);
    }
}
