package cc.architect.commands;

import cc.architect.managers.Parties;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Invite {
    public static void register(LifecycleEventManager<Plugin> manager) {
        // create and register command
        manager.registerEventHandler(LifecycleEvents.COMMANDS,event -> {
            // register command
            event.registrar().register(Commands.literal("invite")
                .then(Commands.literal("send")
                    .then(Commands.argument("player",StringArgumentType.word())
                        .executes(ctx -> {
                            Player sender = (Player) ctx.getSource().getSender();
                            String receiver = StringArgumentType.getString(ctx,"player");
                            if (receiver == null) {
                                return Command.SINGLE_SUCCESS;
                            }
                            Parties.sendInvite(sender,receiver);
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )
                .then(Commands.literal("accept")
                    .executes(ctx -> {
                        if (ctx.getSource() == null) {
                            return Command.SINGLE_SUCCESS;
                        }
                        Player receiver = (Player) ctx.getSource().getSender();

                        Parties.acceptInvite(receiver.getName());
                        return Command.SINGLE_SUCCESS;
                    })
                )
                .then(Commands.literal("deny")
                    .executes(ctx -> {
                        if (ctx.getSource() == null) {
                            return Command.SINGLE_SUCCESS;
                        }
                        Player receiver = (Player) ctx.getSource().getSender();

                        Parties.denyInvite(receiver.getName());
                        return Command.SINGLE_SUCCESS;
                    })
                )
                .build()
            );
        });
    }
}
