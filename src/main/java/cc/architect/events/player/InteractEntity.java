package cc.architect.events.player;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Optional;

import static cc.architect.managers.Dialogues.*;

public class InteractEntity implements Listener {
    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        // get player
        Player p = e.getPlayer();
        // check if clicked entity is an interaction entity
        Entity clicked = e.getRightClicked();
        if (!(clicked instanceof Interaction)) {
            Optional<Entity> optional = clicked.getNearbyEntities(0,0,0).stream().filter(entity -> entity instanceof Interaction).findFirst();
            if (optional.isEmpty()) {
                return;
            }
            clicked = optional.get();
        }
        // cancel event
        e.setCancelled(true);
        // check if player is in dialogue
        if (!dialoguePositions.containsKey(p)) {
            // get first tag of clicked entity
            Optional<String> possibleId = clicked.getScoreboardTags().stream().findFirst();
            // check if the tag exist
            if (possibleId.isEmpty()) {
                return;
            }
            // enter dialogue
            enterDialogue(p,clicked.getLocation(),possibleId.get());
            Bukkit.broadcast(Component.text("Entering dialogue..."));
        } else {
            // check if player has an active response list
            if (!responseLists.containsKey(p)) {
                return;
            }
            // choose next response
            responseLists.get(p).chooseNext(p);
        }
    }
}
