package cc.architect.events.player;

import cc.architect.minigames.mining.BlockBreakHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        BlockBreakHandler.handleBlockBreakEvent(event);
    }
}
