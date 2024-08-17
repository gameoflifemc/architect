package cc.architect.events.player;

import cc.architect.minigames.farming.eventhandlers.FarmingBlockBreakEventHandler;
import cc.architect.minigames.mining.eventhandlers.MiningBlockBreakHandler;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            e.setCancelled(true);
        }
        MiningBlockBreakHandler.handleBlockBreakEvent(e);
        FarmingBlockBreakEventHandler.handleEvent(e);
    }
}
