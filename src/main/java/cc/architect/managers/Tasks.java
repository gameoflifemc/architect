package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.leaderboards.stats.StatsCaching;
import cc.architect.tasks.mining.Replenish;
import cc.architect.tasks.player.ActionBar;
import cc.architect.tasks.player.Autosave;
import cc.architect.tasks.player.Compass;
import cc.architect.tasks.player.Tips;

public class Tasks {
    public static final Replenish replenishBedrockTask = new Replenish();
    private static final int DEFAULT_DELAY = 20;
    public static void registerTasks() {
        // compass
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,new Compass(),DEFAULT_DELAY,1);
        // action bar
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,new ActionBar(),DEFAULT_DELAY,35);
        // autosave
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,new Autosave(),DEFAULT_DELAY,6000);
        // mining
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN, replenishBedrockTask,DEFAULT_DELAY,6000);
        // stats
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,StatsCaching::cacheStats,DEFAULT_DELAY,6000);
        // tips
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,new Tips(),DEFAULT_DELAY,12000);
    }
}
