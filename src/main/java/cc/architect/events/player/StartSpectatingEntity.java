package cc.architect.events.player;

import cc.architect.objects.HashMaps;
import cc.architect.objects.ResponseList;
import com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class StartSpectatingEntity implements Listener {
    @EventHandler
    public void onStartSpectatingEntity(PlayerStartSpectatingEntityEvent e) {
        // get player
        Player p = e.getPlayer();
        // check if player is in dialogue
        if (!HashMaps.DIALOGUE_POSITIONS.containsKey(p)) {
            return;
        }
        // check if player is not spectating themselves
        if (e.getCurrentSpectatorTarget() == p) {
            return;
        }
        // cancel event
        e.setCancelled(true);
        // get response lists
        HashMap<Player, ResponseList> responseLists = HashMaps.RESPONSE_LISTS;
        // check if player has an active response list
        if (!responseLists.containsKey(p)) {
            return;
        }
        // get response list
        responseLists.get(p).confirmOrSend(p);
    }
}
