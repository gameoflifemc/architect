package cc.architect.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import static cc.architect.managers.Dialogue.leaveDialogue;
import static cc.architect.managers.Dialogue.playersInDialogue;

public class ToggleSneak implements Listener {
    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (playersInDialogue.contains(p)) {
            e.setCancelled(true);
            leaveDialogue(p);
        }
    }
}
