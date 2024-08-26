package cc.architect.minigames.travel.mine;

import cc.architect.minigames.travel.wrapper.BasicTravelMinigame;
import cc.architect.minigames.travel.wrapper.Factory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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
    public MineTravel() {
        super(Bukkit.getWorld("mine"),40);
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

    @Override
    public List<Location> entityLocations(){
        return zombieLocations;
    }

    @Override
    public List<ItemStack> onSpawnItems() {
        return List.of(sword);
    }
    @Override
    public List<ItemStack> getAllItems() {
        return List.of(shard,sword);
    }
    @Override
    public List<ItemStack> getRequiredItems() {
        return List.of(shard);
    }

    @Override
    public Factory factory() {
        return new ZombieFactory();
    }
}
