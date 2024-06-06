package cc.architect.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import static cc.architect.managers.Dialogue.dialogueRotations;
import static cc.architect.managers.Dialogue.leaveDialogue;

public class InteractEntity implements Listener {
    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (dialogueRotations.containsKey(p)) {
            e.setCancelled(true);
            leaveDialogue(p);
        }
    }
}
