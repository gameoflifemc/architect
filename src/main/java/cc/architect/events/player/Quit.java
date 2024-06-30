package cc.architect.events.player;

import cc.architect.managers.Dialogues;
import cc.architect.objects.HashMaps;
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
        // remove player from dialogue
        if (HashMaps.DIALOGUE_POSITIONS.containsKey(p)) {
            Dialogues.leave(p);
        }
    }
}
