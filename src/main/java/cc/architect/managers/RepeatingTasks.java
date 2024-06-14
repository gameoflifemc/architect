package cc.architect.managers;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import static cc.architect.Architect.plugin;
import static cc.architect.managers.Dialogue.dialoguePositions;
import static org.bukkit.Bukkit.getScheduler;

public class RepeatingTasks {
    public static void scheduleActionBarTask() {
        getScheduler().runTaskTimer(plugin, () -> {
            for (Player p : dialoguePositions.keySet()) {
                p.sendActionBar(Component.text("Press SHIFT to Leave Dialogue"));
            }
        },0,30);
    }
}
