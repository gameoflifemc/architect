package cc.architect.managers;

import cc.architect.Architect;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;

public class Time {
    private static final int SPEED = 1000;
    private static final int SENSITIVITY = 10;
    public static void set(Player p, int time) {
        p.setPlayerTime(time,false);
    }
    public static void accelerate(Player p, boolean isMorning) {
        // get times
        int start = isMorning ? 18000 : 6000;
        int end = isMorning ? 6000 : 18000;
        // set time just in case
        Time.set(p,start);
        // prepare time counter
        AtomicInteger counter = new AtomicInteger(start);
        // accelerate time until it reaches the target
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,(task) -> {
            // get current time
            int current = counter.addAndGet(SPEED);
            // handle overflow
            if (current >= 24000) {
                current -= 24000;
                counter.set(current);
            }
            // set time
            Time.set(p,current);
            // cancel task if finished
            if (current == end) {
                task.cancel();
            }
        },SENSITIVITY,SENSITIVITY);
    }
}

