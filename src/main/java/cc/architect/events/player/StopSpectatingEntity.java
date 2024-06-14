package cc.architect.events.player;

import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static cc.architect.managers.Dialogue.dialoguePositions;

public class StopSpectatingEntity implements Listener {
    @EventHandler
    public void onStopSpectatingEntity(PlayerStopSpectatingEntityEvent e) {
        if (dialoguePositions.containsKey(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
}
