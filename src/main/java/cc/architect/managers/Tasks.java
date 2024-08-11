package cc.architect.managers;

import cc.architect.tasks.mining.MineOresTimer;
import cc.architect.tasks.mining.ReplenishBedrock;
import cc.architect.tasks.player.ActionPoints;
import org.bukkit.scheduler.BukkitScheduler;

import static cc.architect.Architect.PLUGIN;

public class Tasks {
    public static final ReplenishBedrock replenishBedrockTask = new ReplenishBedrock();
    public static void registerTasks() {
        BukkitScheduler scheduler = PLUGIN.getServer().getScheduler();
        scheduler.runTaskTimer(PLUGIN, replenishBedrockTask,1,1800);
        scheduler.runTaskTimer(PLUGIN,new MineOresTimer(),1,1);
        scheduler.runTaskTimer(PLUGIN,new ActionPoints(),1,30);
    }
}
