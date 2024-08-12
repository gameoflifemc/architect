package cc.architect.events.player;

import cc.architect.minigames.mining.eventhandlers.BlockBreakHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        BlockBreakHandler.handleBlockBreakEvent(e);
    }
}
