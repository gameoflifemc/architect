package cc.architect.events.player;

import cc.architect.Architect;
import cc.architect.managers.Game;
import cc.architect.managers.Meta;
import cc.architect.objects.HashMaps;
import cc.architect.tasks.farming.Farming;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class Interact implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
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
            if (p.getGameMode().equals(GameMode.ADVENTURE) && !p.getWorld().getName().equals("world")) {
                if (!(b.getBlockData() instanceof Door)) {
                    e.setCancelled(true);
                }
            }
            if (item == null) {
                return;
            }
            if (p.getWorld().equals(Architect.FARM)) {
                Farming.interactBlockFarm(e);
            }
        }
        if (!p.getWorld().getName().equals("world")) {
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
