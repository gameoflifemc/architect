package cc.architect.tasks.farming;

import cc.architect.Architect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.List;

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
            new AutoPot(new Location(Architect.FARM,112,-56,-114),new Location(Architect.FARM,118,-56,-108),Material.CARROTS)
    );
    public static final List<Material> farmableSeeds = List.of(Material.WHEAT_SEEDS,Material.CARROT,Material.POTATO,Material.TORCHFLOWER_CROP, Material.BEETROOT_SEEDS, Material.COCOA_BEANS, Material.SEAGRASS, Material.SEA_PICKLE, Material.PITCHER_CROP);
    public static void breakBlockFarm(BlockBreakEvent e){
        Player p = e.getPlayer();

        ItemStack inHand = p.getInventory().getItemInMainHand();
        if(!(inHand.getType().equals(Material.IRON_HOE) || inHand.getType().equals(Material.DIAMOND_HOE)|| inHand.getType().equals(Material.NETHERITE_HOE))) return;

        Block b = e.getBlock();

        b.getDrops(inHand).forEach(drop -> {
            if(farmableSeeds.contains(drop.getType())){
                Bukkit.getServer().dispatchCommand(Architect.CONSOLE, "give " + p.getName() + " " + drop.getType().getKey().getKey() + "[can_place_on={predicates:[{blocks:\"farmland\"}],show_in_tooltip:true}] 1");
            }else{
                p.getInventory().addItem(drop);
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

        for(AutoPot pot : pots){
            if(pot.boundingBox.contains(b.getX(),b.getY(),b.getZ())){
                Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,()->{
                    if(!b.getType().equals(Material.AIR)) return;
                    b.setType(pot.material);
                },80);
                return;
            }
        }
    }

    public static void interactBlockFarm(PlayerInteractEvent e) {
        e.setCancelled(false);
    }
}
