package cc.architect.events.player;

import cc.architect.managers.Dialogues;
import cc.architect.objects.HashMaps;
import cc.architect.objects.ResponseList;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.HashMap;
import java.util.Optional;

public class InteractEntity implements Listener {
    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        // get player
        Player p = e.getPlayer();
        // get clicked entity
        Entity clicked = e.getRightClicked();
        // check if clicked entity is an interaction entity
        if (clicked.getType() != EntityType.INTERACTION) {
            return;
        }
        // cancel event
        e.setCancelled(true);
        // check if player is in dialogue
        if (!HashMaps.DIALOGUE_POSITIONS.containsKey(p)) {
            // get first tag of clicked entity
            Optional<String> possibleUid = clicked.getScoreboardTags().stream().findFirst();
            // check if the tag exist
            if (possibleUid.isEmpty()) {
                return;
            }
            
            // enter dialogue
            Dialogues.enter(p,clicked.getLocation(),possibleUid.get());
            Bukkit.broadcast(Component.text("Entering dialogue..."));
        } else {
            // get response lists
            HashMap<Player, ResponseList> responseLists = HashMaps.RESPONSE_LISTS;
            // check if player has an active response list
            if (!responseLists.containsKey(p)) {
                return;
            }
            // choose next response
            responseLists.get(p).chooseNext(p);
        }
    }
}
