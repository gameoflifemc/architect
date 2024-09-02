package cc.architect.events.block;

import cc.architect.Architect;
import cc.architect.bonuses.DiamondBonus;
import cc.architect.loottables.LootTableManager;
import cc.architect.loottables.definitions.MiningChestLootTable;
import cc.architect.managers.FarmingCycles;
import cc.architect.managers.Items;
import cc.architect.managers.Tasks;
import cc.architect.objects.Messages;
import cc.architect.tasks.farmingLegacy.Mushrooms;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

import static cc.architect.Utilities.rollRandom;

public class Break implements Listener {
    @EventHandler
    public void onBreak(org.bukkit.event.block.BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode().equals(GameMode.ADVENTURE)) {
            e.setCancelled(true);
            World world = p.getWorld();
            Block b = e.getBlock();
            if (world == Architect.MINE) {
                mining(p,b);
            } else if (world == Architect.FARM) {
                //farming(p,b);
            }
        }
    }

    /**
     * Legacy farming method
     * @param p
     * @param b
     */
    private static void farming(Player p, Block b) {
        switch (b.getType()) {
            case WHEAT -> {
                if (canHarvest(p, b)) {
                    p.sendMessage(Messages.FARMING_CANNOT_HARVEST);
                    return;
                }
                p.getInventory().addItem(new ItemStack(Material.WHEAT));
                b.setType(Material.AIR);
            }
            case CARROTS -> {
                if (canHarvest(p, b)) {
                    p.sendMessage(Messages.FARMING_CANNOT_HARVEST);
                    return;
                }
                Ageable ageable = (Ageable) b.getBlockData();
                if (ageable.getAge() == 0) {
                    p.getInventory().addItem(new ItemStack(Material.CARROT));
                    b.setType(Material.AIR);
                }
                else {
                    ageable.setAge(ageable.getAge() - 1);
                    int pitch = new Random().nextInt(90);
                    boolean negativePitch = new Random().nextBoolean();
                    b.setBlockData(ageable);
                    p.setRotation(new Random().nextInt(360),negativePitch ? -pitch : pitch);
                }
            }
            case BROWN_MUSHROOM -> {
                if (canHarvest(p, b)) {
                    p.sendMessage(Messages.FARMING_CANNOT_HARVEST);
                    return;
                }
                if (Mushrooms.mushrooms.containsKey(b.getLocation())) {
                    p.sendMessage(Messages.FARMING_MUSHROOM_GUIDE);
                }
                Mushrooms.mushrooms.put(b.getLocation(), 3);
            }
            case RED_MUSHROOM -> {
                if (canHarvest(p, b)) {
                    p.sendMessage(Messages.FARMING_CANNOT_HARVEST);
                    return;
                }
                p.getInventory().addItem(new ItemStack(Material.RED_MUSHROOM));
                b.setType(Material.AIR);
                b.getLocation().add(0,-1,0).getBlock().setType(Material.DIRT);
            }
        }
    }
    private static void mining(Player p,Block b) {
        switch (b.getType()) {
            case COAL_ORE -> {
                ore(p,b,Material.COAL_ORE,Material.COAL,1000);
            }
            case COPPER_ORE -> {
                ore(p,b,Material.COPPER_ORE,Material.RAW_COPPER,1000);
            }
            case IRON_ORE -> {
                ore(p,b,Material.IRON_ORE,Material.RAW_IRON,2000);
            }
            case GOLD_ORE -> {
                ore(p,b,Material.GOLD_ORE,Material.RAW_GOLD,3000);
            }
            case REDSTONE_ORE -> {
                ore(p,b,Material.REDSTONE_ORE,Material.REDSTONE,3000);
            }
            case LAPIS_ORE -> {
                ore(p,b,Material.LAPIS_ORE,Material.LAPIS_LAZULI,3000);
            }
            case DIAMOND_ORE -> {
                ore(p,b,Material.DIAMOND_ORE,Material.DIAMOND,5000);
                DiamondBonus.add(p,1f/3f);
            }
            case EMERALD_ORE -> {
                ore(p,b,Material.EMERALD_ORE,Material.EMERALD,6000);
            }
            case STONE, ANDESITE, GRANITE -> {
                b.setType(Material.COBBLESTONE);
                //                           this is here to secure the server from crashing
                if (rollRandom(5f) && MiningChestLootTable.miningChestsSpawned < 15) {
                    Location location = b.getLocation();
                    if (location.clone().add(0,1,0).getBlock().getType() != Material.AIR) {
                        p.sendMessage(Messages.TREASURE_FOUND_AUTO_OPEN);
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,SoundCategory.MASTER,100f,1f);
                        p.getInventory().addItem(LootTableManager.roll(new MiningChestLootTable(p)));
                        return;
                    }
                    Location spawn = location.clone().add(0.5,-0.45f,0.5);
                    World world = spawn.getWorld();
                    ArmorStand armorStand = (ArmorStand) world.spawnEntity(spawn, EntityType.ARMOR_STAND);
                    armorStand.getEquipment().setHelmet(Items.CHEST);
                    armorStand.setInvisible(true);
                    armorStand.setGravity(false);
                    p.sendMessage(Messages.TREASURE_FOUND);
                    p.playSound(p.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,SoundCategory.MASTER,100f,1f);
                    MiningChestLootTable.miningChestsSpawned++;
                }
            }
            case COBBLESTONE -> {
                b.setType(Material.BEDROCK);
                if (!Tasks.replenishBedrockTask.hasLocation(b.getLocation())) {
                    Tasks.replenishBedrockTask.addBedrock(b.getLocation(),Material.BEDROCK);
                }
                if (rollRandom(5f)) {
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
        }
    }
    private static void ore(Player p, Block b, Material ore, Material drop, int timer) {
        b.setType(Material.STONE);
        PlayerInventory inventory = p.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        item.damage(1,p);
        int amount = item.getEnchantmentLevel(Enchantment.FORTUNE) + 1;
        inventory.addItem(new ItemStack(drop, amount));
        Tasks.replenishBedrockTask.addBedrock(b.getLocation(),ore);
    }
    private static boolean canHarvest(Player player, Block block) {
        return !((FarmingCycles.profits.containsKey(player) && FarmingCycles.profits.get(player).containsKey(block.getLocation()) && FarmingCycles.profits.get(player).get(block.getLocation())) || !FarmingCycles.getLocations().contains(block.getLocation()));
    }
}
