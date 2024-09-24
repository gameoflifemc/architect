package cc.architect.commands;

import cc.architect.events.player.Respawn;
import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Unstuck {
    public static void register(LifecycleEventManager<Plugin> manager) {
        // create and register command
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            // register command
            event.registrar().register(Commands.literal("unstuck").executes(ctx -> {
                Player p = Bukkit.getPlayerExact(ctx.getSource().getSender().getName());
                if (p == null) {
                    return Command.SINGLE_SUCCESS;
                }
                if (p.getWorld().getName().equals("world")) {
                    return Command.SINGLE_SUCCESS;
                }
                p.teleport(Respawn.getRespawnLocation(p));
                return Command.SINGLE_SUCCESS;
            })
            .build()
            );
        });
    }
}
