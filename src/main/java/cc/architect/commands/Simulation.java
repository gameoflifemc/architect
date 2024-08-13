package cc.architect.commands;

import cc.architect.leaderboards.stats.PlayerStatsHolder;
import cc.architect.leaderboards.stats.StatsCaching;
import cc.architect.leaderboards.stats.StatsSorter;
import cc.architect.managers.Actions;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

import static cc.architect.leaderboards.PlayerStatsBoard.createStatsLeaderBoard;

public class Simulation {
    public static void register(LifecycleEventManager<Plugin> manager) {
        // create and register command
        manager.registerEventHandler(LifecycleEvents.COMMANDS,event -> {
            // register command
            event.registrar().register(Commands.literal("simulation")
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
                    .then(Commands.literal("hide")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .executes(ctx -> {
                                Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                if (p == null) {
                                    return Command.SINGLE_SUCCESS;
                                }
                                // reset points
                                Actions.hidePoints(p);
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    )
                    .then(Commands.literal("show")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .executes(ctx -> {
                                Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                if (p == null) {
                                    return Command.SINGLE_SUCCESS;
                                }
                                // reset points
                                Actions.showPoints(p);
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    )
                )
                .then(Commands.literal("world")
                    .then(Commands.argument("world",StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            for (World world : Bukkit.getWorlds()) {
                                builder.suggest(world.getName());
                            }
                            return builder.buildFuture();
                        })
                        .executes(ctx -> {
                            World world = Bukkit.getWorld(StringArgumentType.getString(ctx,"world"));
                            if (world == null) {
                                return Command.SINGLE_SUCCESS;
                            }
                            Player p = Bukkit.getPlayerExact(ctx.getSource().getSender().getName());
                            if (p == null) {
                                return Command.SINGLE_SUCCESS;
                            }
                            p.teleport(world.getSpawnLocation());
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )
                .then(Commands.literal("test")
                    .executes(ctx -> {
                        createStatsLeaderBoard(Bukkit.getPlayer(ctx.getSource().getSender().getName()).getLocation());
                        return Command.SINGLE_SUCCESS;
                    })
                )
                .then(Commands.literal("update")
                        .executes(ctx -> {
                            StatsCaching.cacheStats();
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build()
            );
        });
    }
}
