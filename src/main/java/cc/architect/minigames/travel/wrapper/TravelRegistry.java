package cc.architect.minigames.travel.wrapper;

import cc.architect.Architect;
import cc.architect.minigames.travel.farm.FarmTravel;
import cc.architect.minigames.travel.mine.MineTravel;
import cc.architect.minigames.travel.village.VillageTravel;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TravelRegistry {
    public static HashMap<String, TravelMinigame> minigames = new HashMap<>();
    public static Map<UUID, Entity> entities = new HashMap<>();

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
        createEntityDeleter();
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
        e.getPlayer().getInventory().remove(MineTravel.key.getType());
    }

    public static void createEntityDeleter() {
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,()->{
            Architect.TRAVEL.getLivingEntities().forEach(entity -> {
                if(entity instanceof Player) return;
                if(!entity.getScoreboardTags().contains(Architect.SESSION)) {
                    entity.remove();
                }
            });
        },40,40);
    }
}
