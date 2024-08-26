package cc.architect.managers;

import cc.architect.Architect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Time {
    public static void get(Player p, long time) {
        p.getPlayerTimeOffset();
    }
    public static void setAbsolute(Player p, long time) {
        p.setPlayerTime(time,false);
    }
    public static void interpolate(Player p, long time) {
        long offset = p.getPlayerTimeOffset();
        long finalTime = offset + time;
        p.setPlayerTime(-(p.getWorld().getTime() - offset),true);
        Bukkit.getScheduler().runTaskLater(Architect.PLUGIN,() -> Time.setAbsolute(p,finalTime),time);
    }
}

