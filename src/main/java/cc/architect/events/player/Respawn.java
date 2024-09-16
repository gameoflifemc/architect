package cc.architect.events.player;

import cc.architect.Architect;
import cc.architect.managers.Game;
import cc.architect.managers.Meta;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Respawn implements Listener {
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        e.setRespawnLocation(switch (p.getWorld().getName()) {
            case "village" -> Architect.VILLAGE.getSpawnLocation();
            case "mine" -> Architect.MINE.getSpawnLocation();
            case "farm" -> Architect.FARM.getSpawnLocation();
            case "travel" -> {
                double x = p.getX();
                if (30 > x) {
                    yield new Location(Architect.TRAVEL,-31,126,-47);
                } else if (30 < x && x < 150) {
                    yield new Location(Architect.TRAVEL,103,126,-42);
                } else {
                    yield new Location(Architect.TRAVEL,211,125,-57);
                }
            }
            default -> Architect.WORLD.getSpawnLocation();
        });
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> {
            p.setFoodLevel(Integer.parseInt(Meta.get(p,Meta.ACTIONS)));
            p.addPotionEffect(Game.REGENERATION);
        },20);
    }
}
