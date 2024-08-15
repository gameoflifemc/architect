package cc.architect.minigames.mining.eventhandlers;

import cc.architect.bonuses.DiamondBonus;
import cc.architect.heads.HeadLoader;
import cc.architect.loottables.definitions.MiningChestLootTable;
import cc.architect.managers.Tasks;
import cc.architect.objects.Messages;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static cc.architect.Utilities.rollRandom;

public class MiningBlockBreakHandler {
    private static final List<Material> ores = List.of(
            Material.EMERALD,
            Material.DIAMOND
    );

    public static final Table<Player, Location, Integer> minedOres = HashBasedTable.create();

    public static void handleBlockBreakEvent(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        switch(block.getType()) {
            case Material.EMERALD_ORE -> {
                if (minedOres.contains(player, block.getLocation())) {
                    event.setCancelled(true);
                    player.sendMessage(Messages.BLOCK_MINED);
                    return;
                }
                event.setCancelled(true);
                block.setType(Material.STONE);
                player.getInventory().addItem(new ItemStack(Material.EMERALD));
                minedOres.put(player, block.getLocation(), 6000);
                Tasks.replenishBedrockTask.addBedrock(block.getLocation(), Material.EMERALD_ORE);
            }

            case Material.DIAMOND_ORE -> {
                if (minedOres.contains(player, block.getLocation())) {
                    event.setCancelled(true);
                    player.sendMessage(Messages.BLOCK_MINED);
                    return;
                }
                event.setCancelled(true);
                block.setType(Material.STONE);
                DiamondBonus.add(player, 1f/3f);
                minedOres.put(player, block.getLocation(), -1);
                Tasks.replenishBedrockTask.addBedrock(block.getLocation(), Material.DIAMOND_ORE);
            }

            case Material.STONE -> {
                event.setCancelled(true);
                block.setType(Material.COBBLESTONE);
                //                           this is here to secure the server from crashing
                if (rollRandom(5f) && MiningChestLootTable.miningChestsSpawned < 15) {
                    Location spawnLocation = block.getLocation().add(0.5,-0.45f,0.5);
                    spawnTreasure(spawnLocation);
                    player.sendMessage(Messages.TREASURE_FOUND);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 100f, 1f);
                    MiningChestLootTable.miningChestsSpawned++;
                }
            }

            case Material.COBBLESTONE -> {
                event.setCancelled(true);
                block.setType(Material.BEDROCK);
                if (!Tasks.replenishBedrockTask.hasLocation(block.getLocation())) {
                    Tasks.replenishBedrockTask.addBedrock(block.getLocation(), Material.BEDROCK);
                }
                if (rollRandom(5f)) {
                    spawnStealer(event);

                    player.sendMessage(Messages.STEAL);
                    player.playSound(player.getLocation(), Sound.ENTITY_CAT_STRAY_AMBIENT, SoundCategory.MASTER, 100f, 1f);
                }
            }

            case Material.ANDESITE -> event.setCancelled(true);
        }
    }

    private static void spawnTreasure(Location spawnLocation) {
        World world = spawnLocation.getWorld();
        ArmorStand armorStand = (ArmorStand) world.spawnEntity(spawnLocation, EntityType.ARMOR_STAND);
        armorStand.getEquipment().setHelmet(HeadLoader.CHEST);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
    }

    private static void spawnStealer(BlockBreakEvent event) {
        Ocelot ocelot = (Ocelot) event.getPlayer().getWorld().spawnEntity(event.getBlock().getLocation().add(0.5,1,0.5), EntityType.OCELOT);
        BlockDisplay blockDisplay = (BlockDisplay) event.getPlayer().getWorld().spawnEntity(event.getBlock().getLocation().add(0.5,1,0.5), EntityType.BLOCK_DISPLAY);
        blockDisplay.setBlock(Material.EMERALD_BLOCK.createBlockData());
        ocelot.addPassenger(blockDisplay);
        ocelot.setTrusting(false);
    }
}
