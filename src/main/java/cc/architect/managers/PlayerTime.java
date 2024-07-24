package cc.architect.managers;

import cc.architect.Architect;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static cc.architect.Utilities.smoothingFunction;
import static org.bukkit.Bukkit.*;

public class PlayerTime {
    public static Map<UUID,Long> playerTime = new HashMap<>();

    public static long getPlayerTime(UUID player){
        return playerTime.get(player);
    }

    public static void setPlayerTime(UUID player, long time) {
        playerTime.put(player, time);
        getPlayer(player).setPlayerTime(time, false);
    }

    public static void addPlayerTime(UUID player, long time) {
        playerTime.putIfAbsent(player, 0L);
        playerTime.put(player, getPlayerTime(player)+time);
        getPlayer(player).setPlayerTime(getPlayerTime(player), false);
    }

    /**
     *
     * @param player player to interpolate time for
     * @param time time of day
     * @param ticks time of duration in ticks
     */
    public static void interpolatePlayerToTime(UUID player, long time, int ticks) {
        long additiveTime = time - getPlayerTime(player);
        long add =  additiveTime / ticks;

        AtomicInteger i = new AtomicInteger(0);

        Bukkit.getScheduler().runTaskTimer(Architect.PLUGIN,(task) ->{
            getPlayer(player).sendMessage("Interpolating time: " + i.get() + " / " + ticks+" ticks to time "+ time+ " add = "+add+" current time = "+getPlayerTime(player));

            addPlayerTime(player, add);
            i.getAndIncrement();

            if (i.get() > ticks) {
                setPlayerTime(player, getPlayerTime(player));
                task.cancel();
            }

        }, 1L, 1L);
    }
}
