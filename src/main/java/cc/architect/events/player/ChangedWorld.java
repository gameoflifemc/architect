package cc.architect.events.player;

import cc.architect.managers.Compasses;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class ChangedWorld implements Listener {
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Compasses.setupLocations(e.getPlayer());
    }
}
