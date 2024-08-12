package cc.architect.tasks.player;

import cc.architect.managers.Actions;
import org.bukkit.Bukkit;

import static cc.architect.managers.Actions.canSeePoints;

public class DisplayActionPoints implements Runnable {
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (!canSeePoints.getOrDefault(p,false)) {
                return;
            }
            Actions.display(p);
        });
    }
}
