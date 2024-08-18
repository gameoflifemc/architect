package cc.architect.minigames.travel.wraper;

import cc.architect.Architect;
import cc.architect.minigames.travel.MineTravel;

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

        createTask();
    }

    private static void createTask(){
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,()->{
            minigames.values().forEach(TravelMinigame::handeExit);
        },0,10);
    }
}
