package cc.architect.tasks.farming;

import cc.architect.managers.FarmingCycles;
import org.bukkit.entity.Player;

public class ShiftSeasons implements Runnable {
    private final Player player;

    public ShiftSeasons(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        FarmingCycles.activeCycleManagers.get(player).moveToNextSeason();
    }
}
