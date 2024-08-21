package cc.architect.minigames.travel.wraper;

import cc.architect.Architect;
import cc.architect.minigames.travel.farm.mine.FarmTravel;
import cc.architect.minigames.travel.mine.MineTravel;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

public class TravelRegistry {
    public static HashMap<String, TravelMinigame> minigames = new HashMap<>();

    public static void register(String name, TravelMinigame minigame) {
        minigames.put(name, minigame);
    }

    public static TravelMinigame get(String name) {
        return minigames.get(name);
    }

    public static void init() {
        register("mine", new MineTravel());
        //register("farm", new FarmTravel());

        createTask();
    }

    private static void createTask(){
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,()->{
            minigames.values().forEach(TravelMinigame::handeExit);
        },0,10);
    }

    public static void entityDeath(EntityDeathEvent event) {
        ((MineTravel)minigames.get("mine")).entityDeath(event);
    }

    public static void entityRemove(EntityRemoveFromWorldEvent event) {
        ((MineTravel)minigames.get("mine")).entityRemove(event);
    }

    public static void playerDeath(PlayerDeathEvent e) {
        e.getPlayer().getInventory().remove(MineTravel.sword.getType());
        e.getPlayer().getInventory().remove(MineTravel.shard.getType());
    }
}
