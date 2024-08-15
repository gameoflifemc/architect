package cc.architect.minigames.farming.eventhandlers;

import cc.architect.minigames.farming.CycleManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

public class FarmingPlayerPlaceBlockHandler {
    public static void handleEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.getType().equals(Material.WHEAT)) {
            if (CycleManager.activeCycleManagers.containsKey(player)) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " wheat_seeds[can_place_on={predicates:[{blocks:\"farmland\"}],show_in_tooltip:true}] 1");
                CycleManager.profits.get(player).put(block.getLocation(), false);
            }
        }
    }
}
