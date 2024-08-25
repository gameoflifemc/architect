package cc.architect.minigames.travel.wrapper;

import cc.architect.Architect;
import cc.architect.minigames.travel.farm.FarmTravel;
import cc.architect.minigames.travel.mine.MineTravel;
import cc.architect.minigames.travel.village.VillageTravel;
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
        register("farm", new FarmTravel());
        register("village", new VillageTravel());

        createTask();
    }

    private static void createTask(){
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,()->{
            minigames.values().forEach(TravelMinigame::handeExit);
        },0,10);
    }

    public static void entityDeath(EntityDeathEvent event) {
        for(TravelMinigame minigame : minigames.values()) {
            if(minigame instanceof BasicTravelMinigame basicTravelMinigame) {
                basicTravelMinigame.entityDeath(event);
            }
        }
    }

    public static void playerDeath(PlayerDeathEvent e) {
        e.getPlayer().getInventory().remove(MineTravel.sword.getType());
        e.getPlayer().getInventory().remove(MineTravel.shard.getType());
        e.getPlayer().getInventory().remove(FarmTravel.scrap.getType());
        e.getPlayer().getInventory().remove(VillageTravel.stick.getType());
    }
}
