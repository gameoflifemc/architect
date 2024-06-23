package cc.architect.events.player;

import com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static cc.architect.managers.Dialogues.dialoguePositions;
import static cc.architect.managers.Dialogues.responseLists;

public class StartSpectatingEntity implements Listener {
    @EventHandler
    public void onStartSpectatingEntity(PlayerStartSpectatingEntityEvent e) {
        // get player
        Player p = e.getPlayer();
        // check if player is in dialogue
        if (!dialoguePositions.containsKey(p)) {
            return;
        }
        // check if player is not spectating themselves
        if (e.getCurrentSpectatorTarget() == p) {
            return;
        }
        // cancel event
        e.setCancelled(true);
        // check if player has an active response list
        if (!responseLists.containsKey(p)) {
            return;
        }
        // get response list
        responseLists.get(p).confirmOrSend(p);
    }
}
