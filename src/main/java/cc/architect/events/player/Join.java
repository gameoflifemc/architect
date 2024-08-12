package cc.architect.events.player;

import cc.architect.Architect;
import cc.architect.bonuses.DiamondBonus;
import cc.architect.channels.ServerName;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // empty join message
        e.joinMessage(Component.empty());
        // get player
        Player p = e.getPlayer();
        // initialize bonus
        DiamondBonus.initPlayer(p);
        // get server name
        if (ServerName.getServerName() == null) {
            Bukkit.getScheduler().runTaskLater(Architect.PLUGIN,ServerName::requestServerName,5);
        }
    }
}
