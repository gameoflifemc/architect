package cc.architect.tasks.player;

import cc.architect.managers.Compasses;
import org.bukkit.Bukkit;

public class Compass implements Runnable {
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(Compasses::updateValues);
    }
}
