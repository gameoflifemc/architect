package cc.architect.events.player;

import cc.architect.routines.Pregame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
        if (block.getType() != Material.STONE_BUTTON) {
            return;
        }
        World world = Bukkit.getWorld("playground");
        if (world == null) {
            return;
        }
        Player p = e.getPlayer();
        p.teleport(new Location(world,0.5,127.0,0.5,0,0));
        Pregame.welcome(p);
    }
}
