package cc.architect.minigames.farming.eventhandlers;

import cc.architect.minigames.farming.CycleManager;
import cc.architect.objects.Messages;
import cc.architect.tasks.farming.Mushrooms;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class FarmingBlockBreakEventHandler {
    public static void handleEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material material = event.getBlock().getType();
        Block block = event.getBlock();

        switch (material) {
            case WHEAT -> {
                if (canHarvest(player, block)) {
                    event.setCancelled(true);
                    player.sendMessage(Messages.FARMING_CANNOT_HARVEST);
                    return;
                }
                player.getInventory().addItem(new ItemStack(Material.WHEAT));
                block.setType(Material.AIR);
                event.setCancelled(true);
            }

            case CARROTS -> {
                if (canHarvest(player, block)) {
                    event.setCancelled(true);
                    player.sendMessage(Messages.FARMING_CANNOT_HARVEST);
                    return;
                }
                event.setCancelled(true);
                Ageable ageable = (Ageable) block.getBlockData();
                int age = ageable.getAge();

                if (age == 0) {
                    player.getInventory().addItem(new ItemStack(Material.CARROT));
                    block.setType(Material.AIR);
                }
                else {
                    ageable.setAge(ageable.getAge()-1);

                    int pitch = new Random().nextInt(90);
                    boolean negativePitch = new Random().nextBoolean();
                    int yaw = new Random().nextInt(360);
                    block.setBlockData(ageable);
                    player.setRotation(yaw, negativePitch ? -pitch : pitch);
                }
            }

            case BROWN_MUSHROOM -> {
                if (canHarvest(player, block)) {
                    event.setCancelled(true);
                    player.sendMessage(Messages.FARMING_CANNOT_HARVEST);
                    return;
                }
                event.setCancelled(true);
                if (Mushrooms.mushrooms.containsKey(block.getLocation())) {
                    player.sendMessage(Messages.FARMING_MUSHROOM_GUIDE);
                }
                Mushrooms.mushrooms.put(block.getLocation(), 3);
            }

            case RED_MUSHROOM -> {
                if (canHarvest(player, block)) {
                    event.setCancelled(true);
                    player.sendMessage(Messages.FARMING_CANNOT_HARVEST);
                    return;
                }
                event.setCancelled(true);
                player.getInventory().addItem(new ItemStack(Material.RED_MUSHROOM));
                block.setType(Material.AIR);
                block.getLocation().add(0,-1,0).getBlock().setType(Material.DIRT);
            }
        }
    }

    private static boolean canHarvest(Player player, Block block) {
        return !((CycleManager.profits.containsKey(player) && CycleManager.profits.get(player).containsKey(block.getLocation()) && CycleManager.profits.get(player).get(block.getLocation())) || !CycleManager.getLocations().contains(block.getLocation()));
    }
}
