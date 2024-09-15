package cc.architect.events.misc;

import cc.architect.tasks.farming.Farming;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

public class BlockGrow implements Listener {
    @EventHandler
    public void BlockGrowEvent(BlockGrowEvent event) {
        Bukkit.broadcastMessage("BlockGrowEvent");
        Farming.handleBlockGrow(event);
    }
}
