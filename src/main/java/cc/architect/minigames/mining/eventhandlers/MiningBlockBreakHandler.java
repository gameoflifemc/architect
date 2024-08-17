package cc.architect.minigames.mining.eventhandlers;

import cc.architect.bonuses.DiamondBonus;
import cc.architect.heads.HeadLoader;
import cc.architect.loottables.LootTableManager;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static cc.architect.Utilities.rollRandom;

public class MiningBlockBreakHandler {
    public static final Table<Player, Location, Integer> minedOres = HashBasedTable.create();
    public static void handleBlockBreakEvent(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        switch (block.getType()) {
            case Material.COAL_ORE -> {
                ore(player,block,Material.COAL_ORE,Material.COAL,1000);
            }
            case Material.COPPER_ORE -> {
                ore(player,block,Material.COPPER_ORE,Material.COPPER_INGOT,1000);
            }
            case Material.IRON_ORE -> {
                ore(player,block,Material.IRON_ORE,Material.RAW_IRON,2000);
            }
            case Material.GOLD_ORE -> {
                ore(player,block,Material.GOLD_ORE,Material.RAW_GOLD,3000);
            }
            case Material.REDSTONE_ORE -> {
                ore(player,block,Material.REDSTONE_ORE,Material.REDSTONE,3000);
            }
            case Material.LAPIS_ORE -> {
                ore(player,block,Material.LAPIS_ORE,Material.LAPIS_LAZULI,3000);
            }
            case Material.DIAMOND_ORE -> {
                ore(player,block,Material.DIAMOND_ORE,Material.DIAMOND,5000);
                DiamondBonus.add(player, 1f/3f);
            }
            case Material.EMERALD_ORE -> {
                ore(player,block,Material.EMERALD_ORE,Material.EMERALD,6000);
            }
            case STONE, ANDESITE, GRANITE -> {
                block.setType(Material.COBBLESTONE);
                //                           this is here to secure the server from crashing
                if (rollRandom(5f) && MiningChestLootTable.miningChestsSpawned < 15) {
                    spawnTreasure(player,block);
                }
            }
            case COBBLESTONE -> {
                block.setType(Material.BEDROCK);
                if (!Tasks.replenishBedrockTask.hasLocation(block.getLocation())) {
                    Tasks.replenishBedrockTask.addBedrock(block.getLocation(), Material.BEDROCK);
                }
                if (rollRandom(5f)) {
                    spawnStealer(player,block);
                }
            }
        }
    }
    private static void ore(Player p,Block b, Material ore, Material drop, int timer) {
        if (minedOres.contains(p,b.getLocation())) {
            p.sendMessage(Messages.BLOCK_MINED);
            return;
        }
        b.setType(Material.STONE);
        p.getInventory().addItem(new ItemStack(drop));
        minedOres.put(p,b.getLocation(),timer);
        Tasks.replenishBedrockTask.addBedrock(b.getLocation(),ore);
    }
    private static void spawnTreasure(Player p,Block b) {
        Location location = b.getLocation();
        if (location.clone().add(0,1,0).getBlock().getType() != Material.AIR) {
            p.sendMessage(Messages.TREASURE_FOUND_AUTO_OPEN);
            p.playSound(p.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,SoundCategory.MASTER,100f,1f);
            p.getInventory().addItem(LootTableManager.roll(new MiningChestLootTable(p)));
            return;
        }
        Location spawnLocation = location.clone().add(0.5,-0.45f,0.5);
        World world = spawnLocation.getWorld();
        ArmorStand armorStand = (ArmorStand) world.spawnEntity(spawnLocation, EntityType.ARMOR_STAND);
        armorStand.getEquipment().setHelmet(HeadLoader.CHEST);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        p.sendMessage(Messages.TREASURE_FOUND);
        p.playSound(p.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,SoundCategory.MASTER,100f,1f);
        MiningChestLootTable.miningChestsSpawned++;
    }
    private static void spawnStealer(Player p,Block b) {
        p.sendMessage(Messages.STEAL);
        p.playSound(p.getLocation(),Sound.ENTITY_CAT_STRAY_AMBIENT,SoundCategory.MASTER,100f,1f);
        Ocelot ocelot = (Ocelot) p.getWorld().spawnEntity(b.getLocation().add(0.5,1,0.5), EntityType.OCELOT);
        BlockDisplay blockDisplay = (BlockDisplay) p.getWorld().spawnEntity(b.getLocation().add(0.5,1,0.5), EntityType.BLOCK_DISPLAY);
        blockDisplay.setBlock(Material.EMERALD_BLOCK.createBlockData());
        ocelot.addPassenger(blockDisplay);
        ocelot.setTrusting(false);
        ocelot.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,PotionEffect.INFINITE_DURATION,1));
    }
}
