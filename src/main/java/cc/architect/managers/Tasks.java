package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.leaderboards.stats.StatsCaching;
import cc.architect.tasks.farming.Mushrooms;
import cc.architect.tasks.farming.StartFarmingMinigame;
import cc.architect.tasks.mining.Replenish;
import cc.architect.tasks.player.Autosave;
import cc.architect.tasks.player.Compass;

public class Tasks {
    public static final Replenish replenishBedrockTask = new Replenish();
    public static void registerTasks() {
        // compass
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,new Compass(),20,2);
        // autosave
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,new Autosave(),20,1200);
        // mining
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN, replenishBedrockTask,20,1800);
        // farming
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,new StartFarmingMinigame(),20,10);
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,new Mushrooms(),20,20);
        // stats
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,StatsCaching::cacheStats,20,24000);
    }
}
