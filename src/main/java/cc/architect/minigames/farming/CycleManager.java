package cc.architect.minigames.farming;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
* Manages the cycles for the farming minigame
**/
public class CycleManager {
    public static final Map<Player, CycleManager> activeCycleManagers = new HashMap<>();

    public final Player player;
    public int state;

    public CycleManager(Player player) {
        activeCycleManagers.put(player, this);
        this.player = player;
    }

    public void start() {
        state = 0;
        //Tasks.scheduler.runTaskLater(Architect.PLUGIN, new ShiftSeasons(this), 2400);
    }

    public void moveToNextSeason() {
        state += 1;
        switch (state) {
            case 1 -> {
                player.getInventory().remove(Material.IRON_HOE);
                player.getInventory().addItem(new ItemStack(Material.WHEAT_SEEDS, 64));
            }
            case 2 -> {

            }
        }
    }
}
