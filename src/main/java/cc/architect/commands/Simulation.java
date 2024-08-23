package cc.architect.commands;

import cc.architect.managers.Actions;
import cc.architect.managers.Meta;
import cc.architect.managers.Movers;
import cc.architect.minigames.travel.wrapper.TravelMinigame;
import cc.architect.minigames.travel.wrapper.TravelRegistry;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Simulation {
    public static void register(LifecycleEventManager<Plugin> manager) {
        // create and register command
        manager.registerEventHandler(LifecycleEvents.COMMANDS,event -> {
            // register command
            event.registrar().register(Commands.literal("simulation")
                // internal commands
                .then(Commands.literal("points")
                    .then(Commands.literal("remove")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .executes(ctx -> {
                                Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                if (p == null) {
                                    return Command.SINGLE_SUCCESS;
                                }
                                // remove point
                                Actions.removePoint(p);
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    )
                )
                .then(Commands.literal("score")
                    .then(Commands.literal("add")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .then(Commands.argument("amount",IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                    if (p == null) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    // write to database
                                    Meta.add(p,"score_total",IntegerArgumentType.getInteger(ctx,"amount"));
                                    return Command.SINGLE_SUCCESS;
                                })
                            )
                        )
                    )
                )
                .then(Commands.literal("travel")
                    .then(Commands.argument("player",StringArgumentType.word())
                        .then(Commands.argument("world",StringArgumentType.word())
                            .executes(ctx -> {
                                Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                String world = StringArgumentType.getString(ctx,"world");
                                if (p == null) {
                                    return Command.SINGLE_SUCCESS;
                                }
                                TravelMinigame minigame = TravelRegistry.get(world);
                                if(minigame == null) {
                                    return Command.SINGLE_SUCCESS;
                                }
                                minigame.playerEnter(p.getUniqueId());
                                
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    )
                )
                // admin commands
                .then(Commands.literal("world")
                    .then(Commands.argument("world",StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            for (World world : Bukkit.getWorlds()) {
                                builder.suggest(world.getName());
                            }
                            return builder.buildFuture();
                        })
                        .executes(ctx -> {
                            Player p = Bukkit.getPlayerExact(ctx.getSource().getSender().getName());
                            if (p == null) {
                                return Command.SINGLE_SUCCESS;
                            }
                            Movers.toWorld(p, StringArgumentType.getString(ctx, "world"));
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )
                .build()
            );
        });
    }
}
