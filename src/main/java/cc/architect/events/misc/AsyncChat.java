package cc.architect.events.misc;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AsyncChat implements Listener {
    @EventHandler
    public void onAsyncChat(AsyncChatEvent e) {
        e.setCancelled(true);
    }
}
