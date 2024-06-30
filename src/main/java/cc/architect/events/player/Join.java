package cc.architect.events.player;

import cc.architect.Architect;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class Join implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // empty join message
        e.joinMessage(Component.empty());
        // get player
        Player p = e.getPlayer();
        // keep other players hidden
        for (Player other : Bukkit.getServer().getOnlinePlayers()) {
            Plugin plugin = Architect.PLUGIN;
            other.hidePlayer(plugin, p);
            p.hidePlayer(plugin, other);
        }
    }
}
