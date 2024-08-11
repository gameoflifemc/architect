package cc.architect.events.player;

import cc.architect.managers.Routines;
import org.bukkit.Material;
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
        if (block.getType() != Material.POLISHED_BLACKSTONE_BUTTON) {
            return;
        }
        Player p = e.getPlayer();
        Routines.enterGame(p);
    }
}
