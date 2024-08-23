package cc.architect.tasks.farming;

import cc.architect.managers.FarmingCycles;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StartFarmingMinigame implements Runnable {

    @Override
    public void run() {
        Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);

        for (Player player : players) {
            if (player.getInventory().contains(new ItemStack(Material.IRON_HOE)) && !FarmingCycles.activeCycleManagers.containsKey(player)) {
                new FarmingCycles(player).start();
            }
        }
    }
}
