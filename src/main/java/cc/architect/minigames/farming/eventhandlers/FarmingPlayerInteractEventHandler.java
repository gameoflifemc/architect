package cc.architect.minigames.farming.eventhandlers;

import cc.architect.Utilities;
import cc.architect.loottables.LootTableManager;
import cc.architect.loottables.definitions.FarmingLootTable;
import cc.architect.minigames.farming.CycleManager;
import cc.architect.objects.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class FarmingPlayerInteractEventHandler {
    public static void handleEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() == null) {
            return;
        }

        if (event.hasBlock()) {
            Material blockType = event.getClickedBlock().getType();
            if (blockType.equals(Material.GRASS_BLOCK) || blockType.equals(Material.DIRT)) {
                if (event.getItem() == null) {
                    return;
                }
                if (event.getItem().getType().equals(Material.IRON_HOE)) {
                    if (CycleManager.tilledLand.contains(event.getClickedBlock().getLocation())) {
                        CycleManager.tilledLand.add(event.getClickedBlock().getLocation());
                    }
                    if (Utilities.rollRandom(5)) {
                        player.getInventory().addItem(LootTableManager.roll(new FarmingLootTable()));
                        player.sendMessage(Messages.FARMING_TREASURE);
                    }
                }
            }

            List<Location> locations = new ArrayList<>();
            for (Player player1 : CycleManager.profits.keySet()) {
                locations.addAll(CycleManager.profits.get(player1).keySet());
            }

            if (event.getItem().getType().equals(Material.BONE_MEAL)) {
                if (event.getClickedBlock().getType().equals(Material.WHEAT) &&
                        (CycleManager.profits.get(player).containsKey(event.getClickedBlock().getLocation()) || !locations.contains(event.getClickedBlock().getLocation()))
                ) {
                    CycleManager.profits.get(player).put(event.getClickedBlock().getLocation(), true);
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " bone_meal[can_place_on={predicates:[{blocks:\"wheat\"}],show_in_tooltip:true}] 1");
                } else {
                    event.setCancelled(true);
                    player.sendMessage(Messages.FARMING_CANNOT_BONEMEAL);
                }
            }
        }
    }
}
