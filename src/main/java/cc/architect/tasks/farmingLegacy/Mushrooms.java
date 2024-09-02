package cc.architect.tasks.farmingLegacy;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class Mushrooms implements Runnable {
    public static Map<Location, Integer> mushrooms = new HashMap<>();

    @Override
    public void run() {
        mushrooms.replaceAll((location, integer) -> mushrooms.get(location) - 1);
        for (Location location : mushrooms.keySet().toArray(new Location[0])) {
            if (mushrooms.get(location) == 0) {
                mushrooms.remove(location);
                location.getBlock().setType(Material.RED_MUSHROOM);
            }
        }
    }
}
