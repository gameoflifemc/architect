package cc.architect.events.player;

import cc.architect.managers.Game;
import cc.architect.managers.Movers;
import cc.architect.managers.Routines;
import cc.architect.minigames.farming.eventhandlers.FarmingPlayerInteractEventHandler;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Interact implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        e.setCancelled(true);
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        FarmingPlayerInteractEventHandler.handleEvent(e);
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        Player p = e.getPlayer();
        switch (block.getType()) {
            case BAMBOO_BUTTON:
                Movers.toVillage(p);
                return;
            case CRIMSON_BUTTON:
                Movers.toFarm(p);
                return;
            case WARPED_BUTTON:
                Movers.toMine(p);
                return;
            case POLISHED_BLACKSTONE_BUTTON:
                if (Routines.current.containsKey(p)) {
                    return;
                }
                Game.begin(p);
                return;
        }
        if (block.getBlockData() instanceof Door) {
            e.setCancelled(false);
        }
    }
}
