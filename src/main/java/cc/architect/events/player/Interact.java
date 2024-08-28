package cc.architect.events.player;

import cc.architect.Architect;
import cc.architect.Utilities;
import cc.architect.loottables.LootTableManager;
import cc.architect.loottables.definitions.FarmingLootTable;
import cc.architect.managers.FarmingCycles;
import cc.architect.managers.Game;
import cc.architect.managers.Meta;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class Interact implements Listener {
    @EventHandler
    public void onInteract(org.bukkit.event.player.PlayerInteractEvent e) {
        Action action = e.getAction();
        if (action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) {
            return;
        }
        Block b = e.getClickedBlock();
        // handle physical events
        if (action == Action.PHYSICAL) {
            if (b == null) {
                return;
            }
            Material blockType = b.getType();
            if (blockType.equals(Material.FARMLAND)) {
                e.setCancelled(true);
            }
            return;
        }
        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        // handle right click events
        if (action == Action.RIGHT_CLICK_BLOCK) {
            if (b == null) {
                return;
            }
            // decide if the event should be cancelled
            if (p.getGameMode().equals(GameMode.ADVENTURE)) {
                if (!(b.getBlockData() instanceof Door)) {
                    e.setCancelled(true);
                }
            }
            if (item == null) {
                return;
            }
            Material itemMat = item.getType();
            Material blockMat = b.getType();
            Location location = b.getLocation();
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
                    Bukkit.getServer().dispatchCommand(Architect.CONSOLE, "give " + p.getName() + " bone_meal[can_place_on={predicates:[{blocks:\"wheat\"}],show_in_tooltip:true}] 1");
                } else {
                    p.sendMessage(Messages.FARMING_CANNOT_BONEMEAL);
                }
            }
        }
        if (p.getWorld() != Architect.WORLD) {
            return;
        }
        if (item == null) {
            return;
        }
        if (!HashMaps.SPYGLASS_USED.contains(p) && item.getType().equals(Material.SPYGLASS)) {
            if (Meta.check(p,Meta.LAST_LOCATION)) {
                Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> Game.resumeDay(p),20);
            } else {
                Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> Game.beginDay(p),20);
            }
            HashMaps.SPYGLASS_USED.add(p);
        }
    }
}
