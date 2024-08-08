package cc.architect.minigames.mining;

import cc.architect.heads.HeadLoader;
import cc.architect.loottables.definitions.MiningChestLootTable;
import cc.architect.objects.Messages;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import static cc.architect.Utilities.chance;

public class BlockBreakHandler {
    public static void handleBlockBreakEvent(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        switch(block.getType()) {
            case Material.EMERALD_ORE -> {
                event.setCancelled(true);
                block.setType(Material.STONE);
                player.getInventory().addItem(new ItemStack(Material.EMERALD));
            }

            case Material.STONE -> {
                event.setCancelled(true);
                block.setType(Material.COBBLESTONE);
                //                           this is here to secure the server from crashing
                if (chance(100f) && MiningChestLootTable.miningChestsSpawned < 15) {
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
                if (chance(100f)) {
                    spawnStealer(event);

                    player.sendMessage(Messages.STEAL);
                    player.playSound(player.getLocation(), Sound.ENTITY_CAT_STRAY_AMBIENT, SoundCategory.MASTER, 100f, 1f);
                }
            }
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
