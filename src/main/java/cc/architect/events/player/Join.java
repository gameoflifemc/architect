package cc.architect.events.player;

import cc.architect.Architect;
import cc.architect.channels.ServerName;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

public class Join implements Listener {
    public static HashMap<String, String> pendingJoin = new HashMap<>();
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // empty join message
        e.joinMessage(Component.empty());
        // get player
        Player p = e.getPlayer();
        // get player name
        String pName = p.getName();
        // get server name
        if (ServerName.getServerName() == null) {
            Bukkit.getScheduler().runTaskLater(Architect.PLUGIN,ServerName::requestServerName,5);
        }
        // organize pending joins
        if (pendingJoin.containsKey(pName)){
            Player pending = Bukkit.getPlayerExact(pendingJoin.get(pName));
            if (pending == null) {
                return;
            }
            p.teleport(pending.getLocation());
            pendingJoin.remove(pName);
        }
    }
}
