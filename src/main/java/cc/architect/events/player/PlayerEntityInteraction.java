package cc.architect.events.player;

import cc.architect.minigames.mining.PlayerEntityInteractionHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class PlayerEntityInteraction implements Listener {
    @EventHandler
    public void playerInteraction(PlayerInteractAtEntityEvent event) {
        PlayerEntityInteractionHandler.handleEvent(event);
    }
}
