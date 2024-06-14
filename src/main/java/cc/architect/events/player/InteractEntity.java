package cc.architect.events.player;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import static cc.architect.managers.Dialogue.dialoguePositions;
import static cc.architect.managers.Dialogue.enterDialogue;

public class InteractEntity implements Listener {
    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (!dialoguePositions.containsKey(p)) {
            Entity clicked = e.getRightClicked();
            if (!(clicked instanceof Interaction)) {
                return;
            }
            Bukkit.broadcast(Component.text("Entering dialogue..."));
            Location target = clicked.getLocation();
            enterDialogue(p, target);
        }
    }
}
