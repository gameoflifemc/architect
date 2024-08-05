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
    public static Map<UUID,Long> playerTime = new HashMap<>();
    public static long getPlayerTime(UUID player){
        return playerTime.get(player);
    }
    public static void setPlayerTime(UUID player, long time) {
        playerTime.put(player, time);
        Player p = getPlayer(player);
        if (p == null) {
            return;
        }
        p.setPlayerTime(time, false);
    }
    public static void addPlayerTime(UUID player, long time) {
        long pTime = getPlayerTime(player);
        playerTime.putIfAbsent(player, 0L);
        playerTime.put(player, pTime + time);
        Player p = getPlayer(player);
        if (p == null) {
            return;
        }
        p.setPlayerTime(pTime, false);
    }
    /**
     *
     * @param player player to interpolate time for
     * @param time time of day
     * @param ticks time of duration in ticks
     */
    public static void interpolatePlayerToTime(UUID player, long time, int ticks) {
        long additiveTime = time - getPlayerTime(player);
        long add = additiveTime / ticks;
        AtomicInteger i = new AtomicInteger(0);
        Bukkit.getScheduler().runTaskTimer(Architect.PLUGIN,(task) -> {
            Player p = Bukkit.getPlayer(player);
            if (p == null) {
                task.cancel();
                return;
            }
            p.sendMessage("Interpolating time: " + i.get() + " / " + ticks + " ticks to time " + time + " add = " + add + " current time = " + getPlayerTime(player));
            addPlayerTime(player,add);
            i.getAndIncrement();
            if (i.get() > ticks) {
                setPlayerTime(player, getPlayerTime(player));
                task.cancel();
            }
        }, 1L, 1L);
    }
}
