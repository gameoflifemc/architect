package cc.architect.events.player;

import cc.architect.managers.Dialogues;
import cc.architect.objects.HashMaps;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class ToggleSneak implements Listener {
    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (HashMaps.DIALOGUE_POSITIONS.containsKey(p)) {
            e.setCancelled(true);
            Dialogues.leave(p);
        }
    }
}
