package cc.architect.tasks.farming;

import cc.architect.Architect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Cocoa;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.List;
import java.util.Map;

public class Farming {
    public static final List<AutoPot> pots = List.of(
            new AutoPot(new Location(Architect.FARM,82,-55,55),new Location(Architect.FARM,85,-55,61),Material.POTATOES),
            new AutoPot(new Location(Architect.FARM,-120,-55,111),new Location(Architect.FARM,-117,-55,114),Material.TORCHFLOWER_CROP),
            new AutoPot(new Location(Architect.FARM,-113,-50,-116),new Location(Architect.FARM,-113,-50,-113),Material.TORCHFLOWER_CROP),
            new AutoPot(new Location(Architect.FARM,-122,-49,-80),new Location(Architect.FARM,-121,-49,78),Material.POTATOES),
            new AutoPot(new Location(Architect.FARM,-112,-56,32),new Location(Architect.FARM,-106,-56,35),Material.BEETROOTS),
            new AutoPot(new Location(Architect.FARM,-97,-51,-85),new Location(Architect.FARM,-93,-51,-79),Material.BEETROOTS),
            new AutoPot(new Location(Architect.FARM,65,-54,-66),new Location(Architect.FARM,73,-54,-59),Material.BEETROOTS),
            new AutoPot(new Location(Architect.FARM,54,-56,79),new Location(Architect.FARM,62,-56,86),Material.CARROTS),
            new AutoPot(new Location(Architect.FARM,115,-56,5),new Location(Architect.FARM,118,-56,7),Material.TORCHFLOWER_CROP),
            new AutoPot(new Location(Architect.FARM,115,-55,119),new Location(Architect.FARM,121,-55,124),Material.TORCHFLOWER_CROP),
            new AutoPot(new Location(Architect.FARM,103,-39,35),new Location(Architect.FARM,103,-39,35),Material.PITCHER_CROP),
            new AutoPot(new Location(Architect.FARM,104,-31,34),new Location(Architect.FARM,106,-31,36),Material.PITCHER_CROP),
            new AutoPot(new Location(Architect.FARM,-51,-41,61),new Location(Architect.FARM,-49,-41,63),Material.PITCHER_CROP),
            new AutoPot(new Location(Architect.FARM,108,-53,40),new Location(Architect.FARM,108,-53,40),Material.PITCHER_CROP),
            new AutoPot(new Location(Architect.FARM,11,-43,-112),new Location(Architect.FARM,14,-43,109),Material.PITCHER_CROP),
            new AutoPot(new Location(Architect.FARM,104,-55,34),new Location(Architect.FARM,107,-51,37),Material.COCOA),
            new AutoPot(new Location(Architect.FARM,11,-54,-112),new Location(Architect.FARM,14,-52,-109),Material.COCOA),
            new AutoPot(new Location(Architect.FARM,112,-56,-114),new Location(Architect.FARM,118,-56,-108),Material.CARROTS)
    );
    public static final List<Material> farmableSeeds = List.of(Material.WHEAT_SEEDS,Material.CARROT,Material.POTATO, Material.BEETROOT_SEEDS);
    public static void breakBlockFarm(BlockBreakEvent e){
        Player p = e.getPlayer();

        ItemStack inHand = p.getInventory().getItemInMainHand();
        if(!(inHand.getType().equals(Material.IRON_HOE) || inHand.getType().equals(Material.DIAMOND_HOE) || inHand.getType().equals(Material.NETHERITE_HOE) || inHand.getType().equals(Material.GOLDEN_HOE))) return;

        Block b = e.getBlock();

        Map<Enchantment, Integer> enchantments = inHand.getEnchantments();
        int repeat = 1;
        if(enchantments.get(Enchantment.FORTUNE)!=null) {
            repeat += enchantments.get(Enchantment.FORTUNE);
        }

        int finalRepeat = repeat;

        if(b.getRelative(0,-1,0).getType().equals(Material.PITCHER_PLANT)) {
            b = b.getRelative(0,-1,0);
        }

        if(b.getType().equals(Material.SEAGRASS)) {
            for (int i = 0; i < finalRepeat; i++) {
                p.getInventory().addItem(new ItemStack(Material.SEAGRASS));
            }
        }

        b.getDrops().forEach(drop -> {
            for(int i = 0; i < finalRepeat; i++) {
                if (farmableSeeds.contains(drop.getType())) {
                    giveFarmland(p, drop);
                } else {
                    p.getInventory().addItem(drop);
                }
            }
        });

        e.setDropItems(false);

        Damageable metaD = (Damageable)inHand.getItemMeta();
        metaD.setDamage(metaD.getDamage() + 1);
        if(metaD.getDamage()==metaD.getMaxDamage()){
            inHand.setAmount(0);
        }else {
            inHand.setItemMeta(metaD);
            e.setCancelled(false);
        }

        Block finalB = b;
        Material finalMaterial = b.getType();
        if(isUnderWater(b)) {
            Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,()->{
                if(finalMaterial.equals(Material.SEA_PICKLE)) {
                    BlockState state = finalB.getState();
                    state.setType(finalMaterial);
                    SeaPickle pickle = (SeaPickle)state.getBlockData();
                    pickle.setPickles((int)Math.floor((Math.random()*2.0)+1));
                    state.setBlockData(pickle);
                    finalB.setBlockData(pickle);
                } else {
                    finalB.setType(finalMaterial);
                }
            },460+(long)(Math.random()*120));
            return;
        }

        for(AutoPot pot : pots){
            if(pot.boundingBox.contains(b.getX(),b.getY(),b.getZ())){

                Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,()->{
                    if(!finalB.getType().equals(Material.AIR)) return;

                    if(!pot.material.equals(Material.COCOA)) {
                        finalB.setType(pot.material);
                    } else {
                        placeCocoa(finalB);
                    }


                },80);
                return;
            }
        }
    }

    public static boolean isUnderWater(Block b) {
        return b.getType().equals(Material.SEAGRASS) || b.getType().equals(Material.SEA_PICKLE);
    }

    public static void interactBlockFarm(PlayerInteractEvent e) {
        e.setCancelled(false);
    }

    public static void giveFarmland(Player p, ItemStack drop) {
        Bukkit.getServer().dispatchCommand(Architect.CONSOLE, "give " + p.getName() + " " + drop.getType().getKey().getKey() + "[can_place_on={predicates:[{blocks:\"farmland\"}],show_in_tooltip:true}] 1");
    }

    public static void handleBlockGrow(BlockGrowEvent event) {
        if(!event.getBlock().getType().equals(Material.PITCHER_CROP)) return;
        event.setCancelled(true);
        event.getBlock().setType(Material.PITCHER_PLANT);

        Block top = event.getBlock().getRelative(0,1,0);
        BlockState state = top.getState();
        state.setType(Material.PITCHER_PLANT);
        Bisected data = (Bisected)state.getBlockData();
        data.setHalf(Bisected.Half.TOP);
        state.setBlockData(data);
        top.setBlockData(data);
    }

    public static void placeCocoa(Block b) {
        if(b.getRelative(BlockFace.EAST).getType().equals(Material.JUNGLE_LOG)) placeCocoa(b,BlockFace.EAST);
        if(b.getRelative(BlockFace.WEST).getType().equals(Material.JUNGLE_LOG)) placeCocoa(b,BlockFace.WEST);
        if(b.getRelative(BlockFace.NORTH).getType().equals(Material.JUNGLE_LOG)) placeCocoa(b,BlockFace.NORTH);
        if(b.getRelative(BlockFace.SOUTH).getType().equals(Material.JUNGLE_LOG)) placeCocoa(b,BlockFace.SOUTH);
    }

    public static void placeCocoa(Block b, BlockFace face) {
        BlockState state = b.getState();
        state.setType(Material.COCOA);
        Cocoa cocoa = (Cocoa)state.getBlockData();
        cocoa.setFacing(face);
        state.setBlockData(cocoa);
        b.setBlockData(cocoa);
    }
}
