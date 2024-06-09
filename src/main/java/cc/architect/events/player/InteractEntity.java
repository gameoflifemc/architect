package cc.architect.events.player;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import static cc.architect.managers.Dialogue.enterDialogue;
import static cc.architect.managers.Dialogue.playersInDialogue;

public class InteractEntity implements Listener {
    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (!playersInDialogue.contains(p)) {
            Bukkit.broadcast(Component.text("Entering dialogue..."));
            Entity clicked = e.getRightClicked();
            Location target;
            if (clicked instanceof Player) {
                target = ((Player) clicked).getEyeLocation();
            } else {
                target = clicked.getLocation();
            }
            enterDialogue(p, target);
        }
    }
}
