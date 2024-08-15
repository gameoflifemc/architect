package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.leaderboards.stats.StatsCaching;
import cc.architect.tasks.farming.Mushrooms;
import cc.architect.tasks.farming.StartFarmingMinigame;
import cc.architect.tasks.mining.MineOresTimer;
import cc.architect.tasks.mining.ReplenishBedrock;
import cc.architect.tasks.player.Autosave;

import static cc.architect.Architect.PLUGIN;

public class Tasks {
    public static final ReplenishBedrock replenishBedrockTask = new ReplenishBedrock();
    public static void registerTasks() {
        Architect.SCHEDULER.runTaskTimer(PLUGIN, replenishBedrockTask,1,1800);
        Architect.SCHEDULER.runTaskTimer(PLUGIN,new MineOresTimer(),1,1);
        Architect.SCHEDULER.runTaskTimer(PLUGIN,new Autosave(),1,1200);
        Architect.SCHEDULER.runTaskTimer(PLUGIN, StatsCaching::cacheStats,20,20*60*20);
        Architect.SCHEDULER.runTaskTimer(PLUGIN, new StartFarmingMinigame(), 1, 10);
        Architect.SCHEDULER.runTaskTimer(PLUGIN, new Mushrooms(), 1, 20);
    }
}
