package cc.architect.events.player;

import cc.architect.Architect;
import cc.architect.bonuses.DiamondBonus;
import cc.architect.channels.ServerName;
import cc.architect.managers.Compasses;
import cc.architect.objects.Compass;
import cc.architect.objects.HashMaps;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {
    private static final int MAX_PLAYERS = 50;
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // empty join message
        e.joinMessage(Component.empty());
        // get player
        Player p = e.getPlayer();
        // check if server is full
        if (Bukkit.getOnlinePlayers().size() >= MAX_PLAYERS && !p.hasPermission("architect.priority")) {
            p.kick(Component.text("Systém zaplněn."));
            return;
        }
        // create compass
        if (!HashMaps.COMPASSES.containsKey(p)) {
            // create compass
            HashMaps.COMPASSES.put(p, new Compass(p));
            // setup locations
            Compasses.setupLocations(p);
        }
        // initialize bonus
        DiamondBonus.initPlayer(p);
        // get server name
        if (ServerName.getServerName() == null) {
            Bukkit.getScheduler().runTaskLater(Architect.PLUGIN,ServerName::requestServerName,5);
        }
    }
}
