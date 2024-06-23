package cc.architect.managers;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import static cc.architect.Architect.plugin;
import static cc.architect.managers.Dialogues.dialoguePositions;
import static cc.architect.managers.Dialogues.responseLists;
import static org.bukkit.Bukkit.getScheduler;

public class RepeatingTasks {
    public static Component standard = Component.text("SHIFT - Leave");
    public static Component response = Component.text("SHIFT - Leave, RIGHT-CLICK - Next Response, LEFT-CLICK - Choose");
    public static Component confirm = Component.text("SHIFT - Leave, LEFT-CLICK - Confirm");
    public static void scheduleActionBarTask() {
        getScheduler().runTaskTimer(plugin, () -> {
            for (Player p : dialoguePositions.keySet()) {
                // initialize message
                Component message;
                // determine message
                if (responseLists.containsKey(p)) {
                    message = responseLists.get(p).isConfirmed() ? confirm : response;
                } else {
                    message = standard;
                }
                // send message
                p.sendActionBar(message);
            }
        },0,30);
    }
}
