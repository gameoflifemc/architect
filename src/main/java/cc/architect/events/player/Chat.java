package cc.architect.events.player;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Chat implements Listener {
    @EventHandler
    public void onChat(AsyncChatEvent e) {
        // completely disable player chat
        e.setCancelled(true);
    }
}
