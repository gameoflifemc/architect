package cc.architect.events.player;

import cc.architect.managers.Meta;
import cc.architect.minigames.travel.wrapper.TravelRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Death implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        TravelRegistry.playerDeath(e);
        Player p = e.getPlayer();
        p.setFoodLevel(Integer.parseInt(Meta.get(p,Meta.ACTIONS)));
    }
}
