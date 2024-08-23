package cc.architect.events.player;

import cc.architect.Utilities;
import cc.architect.loottables.LootTableManager;
import cc.architect.loottables.definitions.FarmingLootTable;
import cc.architect.managers.FarmingCycles;
import cc.architect.managers.Game;
import cc.architect.managers.Movers;
import cc.architect.objects.HashMaps;
import cc.architect.objects.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class Interact implements Listener {
    @EventHandler
    public void onInteract(org.bukkit.event.player.PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (!p.getGameMode().equals(GameMode.ADVENTURE)) {
            return;
        }
        e.setCancelled(true);
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
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
                if (HashMaps.ROUTINES.containsKey(p)) {
                    return;
                }
                Game.begin(p);
                return;
        }
        if (block.getBlockData() instanceof Door) {
            e.setCancelled(false);
        }
        ItemStack item = e.getItem();
        if (item == null) {
            return;
        }
        Material blockMat = block.getType();
        Material itemMat = item.getType();
        Location location = block.getLocation();
        if (blockMat.equals(Material.GRASS_BLOCK) || blockMat.equals(Material.DIRT)) {
            if (itemMat.equals(Material.IRON_HOE)) {
                if (FarmingCycles.tilledLand.contains(location)) {
                    FarmingCycles.tilledLand.add(location);
                }
                if (Utilities.rollRandom(5)) {
                    p.getInventory().addItem(LootTableManager.roll(new FarmingLootTable()));
                    p.sendMessage(Messages.FARMING_TREASURE);
                }
            }
        }
        if (itemMat.equals(Material.BONE_MEAL)) {
            if (blockMat.equals(Material.WHEAT) && (FarmingCycles.profits.get(p).containsKey(location) || !FarmingCycles.getLocations().contains(location))) {
                FarmingCycles.profits.get(p).put(location,true);
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + p.getName() + " bone_meal[can_place_on={predicates:[{blocks:\"wheat\"}],show_in_tooltip:true}] 1");
            } else {
                p.sendMessage(Messages.FARMING_CANNOT_BONEMEAL);
            }
        }
    }
}
