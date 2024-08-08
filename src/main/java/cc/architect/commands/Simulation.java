package cc.architect.commands;

import cc.architect.leaderboards.stats.PlayerStatsHolder;
import cc.architect.leaderboards.stats.StatsSorter;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
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
                //teleports player to world spawn of its world
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
                .then(Commands.literal("leaderboard")
                    .executes(ctx -> {
                        List<PlayerStatsHolder> stats = new ArrayList<>();
                        for(int i = 0; i < 30;i++){
                            stats.add(new PlayerStatsHolder(
                                    (int) (Math.random() * 1000.0),
                                    (int) (Math.random() * 1000),
                                    (int) (Math.random() * 1000),
                                    (int) (Math.random() * 1000),
                                    null,
                                    "Player" + i
                            ));
                        }
                        Bukkit.broadcastMessage("---------Leaderboard---------");
                        for(PlayerStatsHolder stat : stats){
                            Bukkit.broadcastMessage(stat.getName() + " " + stat.getMoney() + " " + stat.getScore() + " " + stat.getDebt() + " " + stat.getNetWorth());
                        }
                        Bukkit.broadcastMessage("");
                        Bukkit.broadcastMessage("------Sorted Leaderboard-----");
                        Bukkit.broadcastMessage("");
                        StatsSorter.listAll(stats, PlayerStatsHolder::getMoney);
                        for(PlayerStatsHolder stat : stats){
                            Bukkit.broadcastMessage(stat.getName() + " " + stat.getMoney() + " " + stat.getScore() + " " + stat.getDebt() + " " + stat.getNetWorth());
                        }
                        Bukkit.broadcastMessage("");
                        Bukkit.broadcastMessage("------top 10-----");
                        Bukkit.broadcastMessage("");
                        for (int i = 0; i < 10; i++) {
                            Bukkit.broadcastMessage(stats.get(i).getName() + " " + stats.get(i).getMoney() + " " + stats.get(i).getScore() + " " + stats.get(i).getDebt() + " " + stats.get(i).getNetWorth());
                        }

                        return Command.SINGLE_SUCCESS;
                    })
                )
                .build()
            );
        });
    }
}
