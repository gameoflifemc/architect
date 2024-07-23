package cc.architect.commands;

import cc.architect.channels.PlayerFinder;
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

import static cc.architect.channels.PlayerLister.getPlayerList;
import static cc.architect.channels.ServerName.requestServerName;
import static org.bukkit.Bukkit.getServer;

public class Party {
    public static void register(LifecycleEventManager<Plugin> manager) {
        // create and register command
        manager.registerEventHandler(LifecycleEvents.COMMANDS,event -> {
            // register command
            event.registrar().register(Commands.literal("party")
                //prints the head of a player
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
                .then(Commands.literal("invite")
                    .then(Commands.argument("player",StringArgumentType.word())
                        .executes(ctx -> {
                            Player sender = (Player) ctx.getSource().getSender();
                            String receiver = StringArgumentType.getString(ctx,"player");
                            if (receiver == null) {
                                return Command.SINGLE_SUCCESS;
                            }
                            cc.architect.managers.Party.sendInvite(sender,receiver);
                            return Command.SINGLE_SUCCESS;
                        })
                        .suggests((ctx, builder) ->{
                            getPlayerList((playerList) -> {
                                playerList.forEach(builder::suggest);
                            });
                            return builder.buildFuture();
                        })
                    )
                )
                .then(Commands.literal("accept")
                    .executes(ctx -> {
                        if (ctx.getSource() == null) {
                            return Command.SINGLE_SUCCESS;
                        }
                        Player receiver = (Player) ctx.getSource().getSender();

                        cc.architect.managers.Party.acceptInvite(receiver.getName());
                        return Command.SINGLE_SUCCESS;
                    })
                )
                .then(Commands.literal("deny")
                    .executes(ctx -> {
                        if (ctx.getSource() == null) {
                            return Command.SINGLE_SUCCESS;
                        }
                        Player receiver = (Player) ctx.getSource().getSender();

                        cc.architect.managers.Party.denyInvite(receiver.getName());
                        return Command.SINGLE_SUCCESS;
                    })
                )
                .then(Commands.literal("test")
                    .executes(ctx -> {
                        if (ctx.getSource() == null) {
                            return Command.SINGLE_SUCCESS;
                        }
                        Player receiver = (Player) ctx.getSource().getSender();
                        requestServerName();
                        Bukkit.broadcastMessage(getServer().getName());
                        return Command.SINGLE_SUCCESS;
                    })
                )
                .then(Commands.literal("findplayer")
                    .then(Commands.argument("player",StringArgumentType.word())
                        .executes(ctx -> {
                            Player sender = (Player) ctx.getSource().getSender();
                            String receiver = StringArgumentType.getString(ctx,"player");
                            if (receiver == null) {
                                return Command.SINGLE_SUCCESS;
                            }
                            sender.sendMessage("Searching for player "+receiver);
                            PlayerFinder.getPlayerServer(receiver,(serverName) -> {
                                sender.sendMessage("Player "+receiver+" found on server "+serverName);
                            });
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )
                .build()
            );
            final Commands commands = event.registrar();
        });
    }
}
