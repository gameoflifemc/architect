package cc.architect.managers;

import cc.architect.Architect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.bukkit.Bukkit.getPlayer;

public class PlayerTime {
    public static void setAbsoluteTime(Player p,long time){
        p.setPlayerTime(time,false);
    }

    public static void setRelativeTime(Player p,long time){
        p.setPlayerTime(time,true);
    }

    public static void addInterpolateTime(Player p,long time){
        long finalTime = p.getPlayerTimeOffset()+time;
        setRelativeTime(p,-(p.getWorld().getTime()-p.getPlayerTimeOffset()));
        Bukkit.getScheduler().runTaskLater(Architect.PLUGIN,() -> {
            setAbsoluteTime(p,finalTime);
        },time);
    }

    public static void setInterpolateTime(Player p,long time){
        setRelativeTime(p,-(p.getWorld().getTime()-p.getPlayerTimeOffset()));
        Bukkit.getScheduler().runTaskLater(Architect.PLUGIN,() -> {
            setAbsoluteTime(p,time);
        },time);
    }
}

