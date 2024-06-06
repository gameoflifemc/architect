package cc.architect.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import static cc.architect.managers.Dialogue.dialogueRotations;
import static cc.architect.managers.Dialogue.leaveDialogue;

public class ToggleSneak implements Listener {
    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (dialogueRotations.containsKey(p)) {
            e.setCancelled(true);
            leaveDialogue(p);
        }
    }
}
