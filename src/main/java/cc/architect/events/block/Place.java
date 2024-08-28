package cc.architect.events.block;

import cc.architect.Architect;
import cc.architect.managers.FarmingCycles;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class Place implements Listener {
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (p.getWorld() != Architect.FARM) {
            return;
        }
        Block b = e.getBlock();
        if (b.getType() != Material.WHEAT) {
            return;
        }
        if (!FarmingCycles.activeCycleManagers.containsKey(p)) {
            return;
        }
        Bukkit.dispatchCommand(Architect.CONSOLE,"give " + p.getName() + " wheat_seeds[can_place_on={predicates:[{blocks:\"farmland\"}],show_in_tooltip:true}] 1");
        FarmingCycles.profits.get(p).put(b.getLocation(),false);
    }
}
