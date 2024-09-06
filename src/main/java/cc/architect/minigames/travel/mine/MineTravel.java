package cc.architect.minigames.travel.mine;

import cc.architect.minigames.travel.wrapper.BasicTravelMinigame;
import cc.architect.minigames.travel.wrapper.Factory;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MineTravel extends BasicTravelMinigame {
    public static final ItemStack key = new ItemStack(Material.STICK);
    public static final ItemStack sword = new ItemStack(Material.WOODEN_SWORD);
    public final List<Location> zombieLocations = List.of(
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
        MAX_COUNT = 9;
        ItemMeta meta = key.getItemMeta();
        meta.displayName(Component.text("Klíč do dolu"));
        meta.setCustomModelData(1);
        key.setItemMeta(meta);
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
        return List.of(key,sword);
    }
    @Override
    public List<ItemStack> getRequiredItems() {
        return List.of(key);
    }

    @Override
    public Factory factory() {
        return new ZombieFactory();
    }
}
