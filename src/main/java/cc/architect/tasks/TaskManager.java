package cc.architect.tasks;

import cc.architect.Architect;
import org.bukkit.scheduler.BukkitScheduler;

import static cc.architect.Architect.PLUGIN;

public class TaskManager {

    public static BukkitScheduler scheduler = PLUGIN.getServer().getScheduler();
    public static ReplenishBedrock replenishBedrockTask = new ReplenishBedrock();

    public static void registerTasks() {
        scheduler.runTaskTimer(PLUGIN, replenishBedrockTask, 0, 1800);
    }
}
