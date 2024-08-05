package cc.architect.commands;

import cc.architect.managers.Avatars;
import cc.architect.managers.PartyManager;
import cc.architect.managers.PlayerTime;
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
                // prints the head of a player
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
                            PartyManager.sendInvite(sender,receiver);
                            return Command.SINGLE_SUCCESS;
                        })
                        /*.suggests((ctx, builder) ->{
                            builder.suggest("test");
                            getPlayerList((playerList) -> {
                                playerList.forEach(builder::suggest);
                            });
                            return builder.buildFuture();
                        })*/
                    )
                )
                .then(Commands.literal("accept")
                    .executes(ctx -> {
                        if (ctx.getSource() == null) {
                            return Command.SINGLE_SUCCESS;
                        }
                        Player receiver = (Player) ctx.getSource().getSender();

                        PartyManager.acceptInvite(receiver.getName());
                        return Command.SINGLE_SUCCESS;
                    })
                )
                .then(Commands.literal("deny")
                    .executes(ctx -> {
                        if (ctx.getSource() == null) {
                            return Command.SINGLE_SUCCESS;
                        }
                        Player receiver = (Player) ctx.getSource().getSender();

                        PartyManager.denyInvite(receiver.getName());
                        return Command.SINGLE_SUCCESS;
                    })
                )
                /*.then(Commands.literal("leave")
                        .executes(ctx -> {
                            if (ctx.getSource() == null) {
                                return Command.SINGLE_SUCCESS;
                            }
                            Player receiver = (Player) ctx.getSource().getSender();

                            PartyManager.leaveParty(receiver.getName());
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
                )*/
                .build()
            );
            event.registrar().register(Commands.literal("setTime")
                    .then(Commands.argument("time",StringArgumentType.word())
                        .then(Commands.argument("interpolate",StringArgumentType.word())
                                .executes(ctx -> {
                                    Player sender = (Player) ctx.getSource().getSender();
                                    String time = StringArgumentType.getString(ctx,"time");
                                    String interpolation = StringArgumentType.getString(ctx,"interpolate");

                                    PlayerTime.setPlayerTime(sender.getUniqueId(), 0L);
                                    PlayerTime.interpolatePlayerToTime(sender.getUniqueId(), Long.parseLong(time), Integer.parseInt(interpolation));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                    )
                .build()
            );
        });
    }
}
