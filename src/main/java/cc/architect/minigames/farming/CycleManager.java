package cc.architect.minigames.farming;

import cc.architect.Architect;
import cc.architect.loottables.LootTableManager;
import cc.architect.loottables.definitions.FarmingCropLootTable;
import cc.architect.objects.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Manages the cycles for the farming minigame
**/
public class CycleManager {
    public static final Map<Player, CycleManager> activeCycleManagers = new HashMap<>();
    public static Map<Player, Map<Location, Boolean>> profits = new HashMap<>();
    public final Player player;
    public int state;

    public CycleManager(Player player) {
        activeCycleManagers.put(player, this);
        this.player = player;
    }

    public void start() {
        state = 0;
        player.sendMessage(Messages.FARMING_TILL);
        shift(15);
    }

    public void moveToNextSeason() {
        state += 1;
        switch (state) {
            case 1 -> {
                player.getInventory().remove(Material.IRON_HOE);
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " wheat_seeds[can_place_on={predicates:[{blocks:\"farmland\"}],show_in_tooltip:true}] 64");
                profits.put(player, new HashMap<>());
                player.sendMessage(Messages.FARMING_SEEDS);
                shift(15);
            }
            case 2 -> {
                player.getInventory().remove(Material.WHEAT_SEEDS);
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " bone_meal[can_place_on={predicates:[{blocks:\"wheat\"}],show_in_tooltip:true}] 64");
                player.sendMessage(Messages.FARMING_FERTILIZE);
                shift(15);
            }
            case 3 -> {
                growCrops();
                player.getInventory().remove(Material.BONE_MEAL);
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " diamond_hoe[minecraft:can_break={blocks:[\"minecraft:wheat\", \"minecraft:nether_wart\", \"brown_mushroom\", \"red_mushroom\"]}, minecraft:unbreakable={}]");
                player.sendMessage(Messages.FARMING_HARVEST);
                shift(60);
            }
            case 4 -> {
                player.getInventory().remove(Material.DIAMOND_HOE);
                player.sendMessage(Messages.FARMING_END);
                activeCycleManagers.remove(player);
                profits.remove(player);
            }
        }
    }

    private void shift(int delay) {
        delay *= 20;
        Bukkit.getScheduler().runTaskLater(Architect.PLUGIN, new ShiftSeasons(player), delay);
    }

    private void growCrops() {
        Location[] locations = profits.get(player).keySet().toArray(new Location[0]);
        player.sendMessage(profits.get(player).keySet().toString());
        for (Location location : locations) {
            if (profits.get(player).get(location)) {
                Material material = LootTableManager.roll(new FarmingCropLootTable()).getType();

                switch (material) {
                    case WHEAT -> {
                        location.getBlock().setType(Material.WHEAT);
                        Ageable ageable = (Ageable) location.getBlock().getBlockData();
                        ageable.setAge(ageable.getMaximumAge());
                        location.getBlock().setBlockData(ageable);
                    }

                    case BROWN_MUSHROOM -> {
                        location.getBlock().setType(Material.AIR);
                        location.add(0,-1,0).getBlock().setType(Material.PODZOL);
                        location.add(0,1,0).getBlock().setType(Material.BROWN_MUSHROOM);
                    }

                    case CARROT -> {
                        location.getBlock().setType(Material.CARROTS);
                        Ageable ageable = (Ageable) location.getBlock().getBlockData();
                        ageable.setAge(ageable.getMaximumAge());
                        location.getBlock().setBlockData(ageable);
                    }
                }
            }
        }
    }

    public static List<Location> getLocations() {
        List<Location> locations = new ArrayList<>();
        for (Player player1 : CycleManager.profits.keySet()) {
            locations.addAll(CycleManager.profits.get(player1).keySet());
        }
        return locations;
    }
}
