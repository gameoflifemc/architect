package cc.architect.events.player;

import cc.architect.tasks.player.Autosave;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Quit implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        // empty quit message
        e.quitMessage(Component.empty());
        // get player
        Player p = e.getPlayer();
        // save last location
        Autosave.location(p);
        // save emeralds
        Autosave.emeralds(p);
    }
}
