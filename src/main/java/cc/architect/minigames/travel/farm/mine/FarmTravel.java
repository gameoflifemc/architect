package cc.architect.minigames.travel.farm.mine;

import cc.architect.minigames.travel.wrapper.BasicTravelMinigame;
import cc.architect.minigames.travel.wrapper.Factory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FarmTravel extends BasicTravelMinigame {
    public static ItemStack scrap = new ItemStack(Material.NETHERITE_SCRAP);
    public static ItemStack sword = new ItemStack(Material.WOODEN_SWORD);
    public List<Location> boggedLocations = List.of(
        new Location(travelWorld, 86, 127, 1),
        new Location(travelWorld, 129, 127, -5),
        new Location(travelWorld, 84, 127, 26)
    );
    public FarmTravel() {
        super(Bukkit.getWorld("farm"),40);
    }

    @Override
    public double getDistance() {
        return 5;
    }

    @Override
    public Location getStartLocation() {
        return new Location(travelWorld, 103, 126, -42,-11, 0);
    }

    @Override
    public Location getEndLocation() {
        return new Location(travelWorld, 129, 129, 61);
    }

    @Override
    public Location teleportToLocation() {
        return new Location(endWorld, 26, -53, -4,103, -22);
    }

    @Override
    public List<Location> entityLocations() {
        return boggedLocations;
    }

    @Override
    public List<ItemStack> onSpawnItems() {
        return List.of(sword);
    }

    @Override
    public List<ItemStack> getRequiredItems() {
        return List.of(scrap);
    }

    @Override
    public List<ItemStack> getAllItems() {
        return List.of(sword, scrap);
    }

    @Override
    public Factory factory() {
        return new BoggedFactory();
    }
}
