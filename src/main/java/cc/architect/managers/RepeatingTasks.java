package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.objects.HashMaps;
import cc.architect.objects.Messages;
import cc.architect.objects.ResponseList;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class RepeatingTasks {
    public static void scheduleActionBarTask() {
        Bukkit.getScheduler().runTaskTimer(Architect.PLUGIN,() -> {
            for (Player p : HashMaps.RESPONSE_LISTS.keySet()) {
                // initialize message
                Component message;
                // determine message
                HashMap<Player, ResponseList> responseLists = HashMaps.RESPONSE_LISTS;
                if (responseLists.containsKey(p)) {
                    message = responseLists.get(p).isConfirmed() ? Messages.CONFIRM : Messages.RESPONSE;
                } else {
                    message = Messages.STANDARD;
                }
                // send message
                p.sendActionBar(message);
            }
        },0,30);
    }
}
