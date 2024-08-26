package cc.architect.managers;

import cc.architect.Architect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Time {
    public static void interpolate(Player p, long from, long to) {
        p.setPlayerTime(from - p.getWorld().getTime(),true);
        Bukkit.getScheduler().runTaskLater(Architect.PLUGIN,() -> p.setPlayerTime(to,false),to - from);
    }
}

