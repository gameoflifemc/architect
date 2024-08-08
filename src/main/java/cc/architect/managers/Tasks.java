package cc.architect.managers;

import cc.architect.tasks.ReplenishBedrock;
import org.bukkit.scheduler.BukkitScheduler;

import static cc.architect.Architect.PLUGIN;

public class Tasks {
    public static BukkitScheduler scheduler = PLUGIN.getServer().getScheduler();
    public static ReplenishBedrock replenishBedrockTask = new ReplenishBedrock();
    public static void registerTasks() {
        scheduler.runTaskTimer(PLUGIN, replenishBedrockTask, 0, 1800);
    }
}
