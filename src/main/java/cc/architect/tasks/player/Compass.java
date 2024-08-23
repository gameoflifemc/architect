package cc.architect.tasks.player;

import cc.architect.managers.Compasses;
import org.bukkit.Bukkit;

public class Compass implements Runnable {
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (p.getWorld().getName().equals("world")) {
                return;
            }
            Compasses.updateValues(p);
        });
    }
}
