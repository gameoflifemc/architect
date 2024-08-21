package cc.architect.events.player;

import cc.architect.minigames.travel.wraper.TravelRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        TravelRegistry.playerDeath(e);
    }
}
