package cc.architect.minigames.travel.village;

import cc.architect.minigames.travel.wrapper.BasicTravelMinigame;
import cc.architect.minigames.travel.wrapper.Factory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class VillageTravel extends BasicTravelMinigame {
    public static ItemStack stick = new ItemStack(Material.STICK);
    public static ItemStack sword = new ItemStack(Material.WOODEN_SWORD);
    public List<Location> huskLocations = List.of(
        new Location(travelWorld, 239,126,-16),
        new Location(travelWorld, 214,126,81),
        new Location(travelWorld, 192,126,85),
        new Location(travelWorld, 246,126,45),
        new Location(travelWorld, 173,126,-58),
        new Location(travelWorld, 171,126,19)
    );
    public VillageTravel() {
        super(Bukkit.getWorld("village"),40);
    }

    @Override
    public double getDistance() {
        return 5;
    }

    @Override
    public Location getStartLocation() {
        return new Location(travelWorld, 211, 125, -57,24, 3);
    }

    @Override
    public Location getEndLocation() {
        return new Location(travelWorld, 174, 126, 70);
    }

    @Override
    public Location teleportToLocation() {
        return new Location(endWorld, -21, 77, 22,-175,0);
    }

    @Override
    public List<Location> entityLocations(){
        return huskLocations;
    }

    @Override
    public List<ItemStack> onSpawnItems() {
        return List.of(sword);
    }
    @Override
    public List<ItemStack> getAllItems() {
        return List.of(stick,sword);
    }
    @Override
    public List<ItemStack> getRequiredItems() {
        return List.of(stick);
    }

    @Override
    public Factory factory() {
        return new HuskFactory();
    }
}
