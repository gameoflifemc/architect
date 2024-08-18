package cc.architect.minigames.travel.wraper;

import org.bukkit.event.player.PlayerQuitEvent;

public class TravelPlayerQuitListener {
    public static void handerPlayerQuitEvent(PlayerQuitEvent e) {
        e.getPlayer().getUniqueId();

        for(TravelMinigame minigame : TravelRegistry.minigames.values()) {
            minigame.removePlayer(e.getPlayer().getUniqueId());
        }
    }
}
