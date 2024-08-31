package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.leaderboards.stats.StatsCaching;
import cc.architect.tasks.farming.Mushrooms;
import cc.architect.tasks.farming.StartFarmingMinigame;
import cc.architect.tasks.mining.Replenish;
import cc.architect.tasks.player.ActionBar;
import cc.architect.tasks.player.Autosave;
import cc.architect.tasks.player.Compass;

public class Tasks {
    public static final Replenish replenishBedrockTask = new Replenish();
    private static final int DELAY = 20;
    public static void registerTasks() {
        // compass
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,new Compass(),DELAY,2);
        // action bar
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,new ActionBar(),DELAY,35);
        // autosave
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,new Autosave(),DELAY,1200);
        // mining
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN, replenishBedrockTask,DELAY,1800);
        // farming
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,new StartFarmingMinigame(),DELAY,10);
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,new Mushrooms(),DELAY,20);
        // stats
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,StatsCaching::cacheStats,DELAY,24000);
    }
}
