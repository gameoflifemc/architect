package cc.architect.minigames.farming;

import org.bukkit.entity.Player;

public class ShiftSeasons implements Runnable {
    private final Player player;

    public ShiftSeasons(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        CycleManager.activeCycleManagers.get(player).moveToNextSeason();
    }
}
