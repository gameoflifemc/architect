package cc.architect.events.player;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static cc.architect.managers.Dialogues.dialoguePositions;
import static cc.architect.managers.Dialogues.leaveDialogue;

public class Quit implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        // empty quit message
        e.quitMessage(Component.empty());
        // get player
        Player p = e.getPlayer();
        // remove player from dialogue
        if (dialoguePositions.containsKey(p)) {
            leaveDialogue(p);
        }
    }
}
