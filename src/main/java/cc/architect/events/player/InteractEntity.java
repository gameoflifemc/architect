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

import static cc.architect.managers.Dialogues.dialoguePositions;
import static cc.architect.managers.Dialogues.enterDialogue;

public class InteractEntity implements Listener {
    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (!dialoguePositions.containsKey(p)) {
            Entity clicked = e.getRightClicked();
            if (!(clicked instanceof Interaction)) {
                return;
            }
            // get tags of clicked entity
            Optional<String> possibleId = clicked.getScoreboardTags().stream().findFirst();
            // check if tags exist
            if (possibleId.isEmpty()) {
                return;
            }
            // enter dialogue
            enterDialogue(p,clicked.getLocation(),possibleId.get());
            Bukkit.broadcast(Component.text("Entering dialogue..."));
        }
    }
}
