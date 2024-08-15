package cc.architect.events.player;

import cc.architect.minigames.mining.eventhandlers.MiningPlayerEntityInteractionHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class InteractAtEntity implements Listener {
    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent e) {
        MiningPlayerEntityInteractionHandler.handleEvent(e);
    }
}
