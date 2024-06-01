package cc.architect.events.player;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static cc.architect.Architect.plugin;
import static org.bukkit.Bukkit.getServer;

public class Join implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // empty join message
        e.joinMessage(Component.empty());
        // get player
        Player p = e.getPlayer();
        // keep other players hidden
        for (Player other : getServer().getOnlinePlayers()) {
            other.hidePlayer(plugin, p);
            p.hidePlayer(plugin, other);
        }
    }
}
