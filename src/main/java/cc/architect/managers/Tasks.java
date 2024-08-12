package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.tasks.mining.MineOresTimer;
import cc.architect.tasks.mining.ReplenishBedrock;
import cc.architect.tasks.player.DisplayActionPoints;

import static cc.architect.Architect.PLUGIN;

public class Tasks {
    public static final ReplenishBedrock replenishBedrockTask = new ReplenishBedrock();
    public static void registerTasks() {
        Architect.SCHEDULER.runTaskTimer(PLUGIN, replenishBedrockTask,1,1800);
        Architect.SCHEDULER.runTaskTimer(PLUGIN,new MineOresTimer(),1,1);
        Architect.SCHEDULER.runTaskTimer(PLUGIN,new DisplayActionPoints(),1,35);
    }
}
