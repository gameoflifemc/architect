package cc.architect.events.player;

import cc.architect.minigames.farming.eventhandlers.FarmingPlayerPlaceBlockHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        FarmingPlayerPlaceBlockHandler.handleEvent(event);
    }
}
