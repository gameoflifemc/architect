package cc.architect.events.misc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChange implements Listener {
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        // disable vanilla hunger mechanic
        e.setCancelled(true);
    }
}
