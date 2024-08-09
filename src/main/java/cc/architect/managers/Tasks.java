package cc.architect.managers;

import cc.architect.tasks.mining.MineOresTimer;
import cc.architect.tasks.mining.ReplenishBedrock;
import org.bukkit.scheduler.BukkitScheduler;

import static cc.architect.Architect.PLUGIN;

public class Tasks {
    public static BukkitScheduler scheduler = PLUGIN.getServer().getScheduler();
    public static ReplenishBedrock replenishBedrockTask = new ReplenishBedrock();
    public static MineOresTimer mineOresTimer = new MineOresTimer();
    public static void registerTasks() {
        scheduler.runTaskTimer(PLUGIN, replenishBedrockTask, 0, 1800);
        scheduler.runTaskTimer(PLUGIN, mineOresTimer, 1, 1);
    }
}
