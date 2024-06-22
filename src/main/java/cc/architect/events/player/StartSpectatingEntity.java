package cc.architect.events.player;

import com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static cc.architect.managers.Dialogues.dialoguePositions;

public class StartSpectatingEntity implements Listener {
    @EventHandler
    public void onStartSpectatingEntity(PlayerStartSpectatingEntityEvent e) {
        Player p = e.getPlayer();
        if (dialoguePositions.containsKey(p)) {
            if (e.getCurrentSpectatorTarget() == p) {
                return;
            }
            e.setCancelled(true);
        }
    }
}
