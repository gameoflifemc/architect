package cc.architect.managers;

import cc.architect.Architect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Time {
    public static void setAbsoluteTime(Player p, long time) {
        p.setPlayerTime(time,false);
    }
    public static void addInterpolateTime(Player p, long time) {
        long finalTime = p.getPlayerTimeOffset() + time;
        p.setPlayerTime(-(p.getWorld().getTime() - p.getPlayerTimeOffset()),true);
        Bukkit.getScheduler().runTaskLater(Architect.PLUGIN,() -> Time.setAbsoluteTime(p, finalTime),time);
    }
}

