package cc.architect.events.player;

import cc.architect.managers.Game;
import cc.architect.managers.Movers;
import cc.architect.managers.Routines;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Interact implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        Player p = e.getPlayer();
        switch (block.getType()) {
            case BAMBOO_BUTTON:
                Movers.toVillage(p);
                break;
            case CRIMSON_BUTTON:
                Movers.toFarm(p);
                break;
            case WARPED_BUTTON:
                Movers.toMine(p);
                break;
            case POLISHED_BLACKSTONE_BUTTON:
                if (Routines.current.containsKey(p)) {
                    break;
                }
                Game.begin(p);
                break;
        }
    }
}
