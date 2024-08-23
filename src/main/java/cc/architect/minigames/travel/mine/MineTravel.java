package cc.architect.minigames.travel.mine;

import cc.architect.minigames.travel.wrapper.BasicTravelMinigame;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;

public class MineTravel extends BasicTravelMinigame {
    public static ItemStack shard = new ItemStack(Material.PRISMARINE_SHARD);
    public static ItemStack sword = new ItemStack(Material.WOODEN_SWORD);
    public List<Location> zombieLocations = List.of(
        new Location(travelWorld, -42, 126, 23),
        new Location(travelWorld, -13, 125, 11),
        new Location(travelWorld, -10, 126, -17),
        new Location(travelWorld, -35, 126, 42),
        new Location(travelWorld, -19, 126, -43),
        new Location(travelWorld, -45, 126, -23),
        new Location(travelWorld, -45, 126, -2)
    );
    public int zombieCount = 0;
    public static final int MAX_ZOMBIE = 15;
    public MineTravel() {
        super(Bukkit.getWorld("mine"),40);
    }

    @Override
    public void start(UUID player) {
        getPlayer(player).getInventory().addItem(new ItemStack(Material.WOODEN_SWORD));
        zombieLocations.forEach(loc->{
            if(zombieCount>MAX_ZOMBIE) return;

            ZombieFactory.createMinerZombie(loc);
            zombieCount++;
        });
    }

    @Override
    public void updateCall() {
        if(zombieCount>MAX_ZOMBIE) return;

        Location loc = zombieLocations.get(new Random().nextInt(zombieLocations.size()));

        if(loc.getNearbyEntities(5,5,5).stream().filter(e->!(e instanceof Player)).count()>3) return;

        ZombieFactory.createMinerZombie(loc);
        zombieCount++;
    }

    @Override
    public boolean canExit(UUID player) {
        boolean out = getPlayer(player).getInventory().contains(shard.getType());
        if(out) {
            getPlayer(player).getInventory().remove(shard.getType());
            getPlayer(player).getInventory().remove(sword.getType());
        }
        return out;
    }

    public void entityDeath(EntityDeathEvent event) {
        if(!event.getEntityType().equals(EntityType.ZOMBIE)) return;
        if(!event.getEntity().getLocation().getWorld().getName().equals(travelWorld.getName())) return;

        zombieCount = Math.max(0,zombieCount-1);
    }

    public void entityRemove(EntityRemoveFromWorldEvent event) {
    }

    @Override
    public double getDistance() {
        return 5;
    }

    @Override
    public Location getStartLocation() {
        return new Location(travelWorld, -31, 126, -47);
    }

    @Override
    public Location getEndLocation() {
        return new Location(travelWorld, -10, 126, 40);
    }

    @Override
    public Location teleportToLocation() {
        return new Location(endWorld, 32, -60, -106,-54,0);
    }
}
