package cc.architect.commands;

import cc.architect.managers.Avatars;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Party {
    public static void register(LifecycleEventManager<Plugin> manager) {
        // create and register command
        manager.registerEventHandler(LifecycleEvents.COMMANDS,event -> {
            // register command
            event.registrar().register(Commands.literal("party")
                .then(Commands.literal("head")
                    .then(Commands.argument("player",StringArgumentType.word())
                        .executes(ctx -> {
                            Player p = Bukkit.getPlayer(StringArgumentType.getString(ctx,"player"));
                            if (p == null) {
                                return Command.SINGLE_SUCCESS;
                            }
                            Component head;
                            try {
                                head = Avatars.create(p);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            p.sendMessage(head);
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )
                .build()
            );
        });
    }
}
