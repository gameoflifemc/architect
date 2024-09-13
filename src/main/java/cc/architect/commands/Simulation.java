package cc.architect.commands;

import cc.architect.Architect;
import cc.architect.Utilities;
import cc.architect.commands.money.Loan;
import cc.architect.commands.money.Savings;
import cc.architect.commands.money.investments.InvestmentBasic;
import cc.architect.commands.money.investments.InvestmentRisky;
import cc.architect.managers.Game;
import cc.architect.managers.Meta;
import cc.architect.managers.Movers;
import cc.architect.managers.Routines;
import cc.architect.minigames.travel.wrapper.TravelMinigame;
import cc.architect.minigames.travel.wrapper.TravelRegistry;
import cc.architect.objects.Colors;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.atomic.AtomicInteger;

import static cc.architect.leaderboards.stats.StatsCaching.cacheStats;
import static cc.architect.managers.Meta.LOAN_RISKY_COUNTER;
import static org.bukkit.Bukkit.getServer;

public class Simulation {
    public static void register(LifecycleEventManager<Plugin> manager) {
        // create and register command
        manager.registerEventHandler(LifecycleEvents.COMMANDS,event -> {
            // register command
            // check player
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
                                // get current value
                                int current = Integer.parseInt(Meta.get(p,Meta.ACTIONS));
                                // if player has no points, move to next routine
                                if (current <= 2) {
                                    Routines.switchToNext(p);
                                    p.setFoodLevel(0);
                                } else {
                                    // calculate new value
                                    int next = current - 2;
                                    // update database
                                    Meta.set(p,Meta.ACTIONS,String.valueOf(next));
                                    // update player
                                    p.setFoodLevel(next);
                                    p.sendMessage(Component.text("Akční bod byl úspěšně využit. Zbývá " + next + " akčních bodů do další části dne.").color(Colors.GREEN));
                                }
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
                                    int amount = IntegerArgumentType.getInteger(ctx,"amount");
                                    // write to database
                                    Meta.add(p,Meta.SCORE_TOTAL,amount);
                                    p.sendMessage(Component.text("+ " + amount + " bodů skóre. Celkem " + Meta.get(p,Meta.SCORE_TOTAL) + " bodů skóre.").color(Colors.GREEN));
                                    return Command.SINGLE_SUCCESS;
                                })
                            )
                        )
                    )
                )
                .then(Commands.literal("savings")
                    .then(Commands.literal("put")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .then(Commands.argument("amount",IntegerArgumentType.integer())
                                .executes(Savings::put)
                            )
                        )
                    )
                    .then(Commands.literal("claim")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .executes(Savings::claim)
                        )
                    )
                    .then(Commands.literal("inspect")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .executes(Savings::inspect)
                        )
                    )
                )
                .then(Commands.literal("investment")
                    .then(Commands.literal("put")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .then(Commands.argument("amount",IntegerArgumentType.integer())
                                .then(Commands.argument("time",IntegerArgumentType.integer())
                                    .executes(InvestmentBasic::investPut)
                                )
                            )
                        )
                    )
                    .then(Commands.literal("claim")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .executes(InvestmentBasic::investClaim)
                        )
                    )
                    .then(Commands.literal("inspect")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .executes(InvestmentBasic::investInspect)
                        )
                    )
                )
                .then(Commands.literal("riskyinvestment")
                    .then(Commands.literal("put")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .then(Commands.argument("amount",IntegerArgumentType.integer())
                                .then(Commands.argument("time",IntegerArgumentType.integer())
                                    .executes(InvestmentRisky::investPut)
                                )
                            )
                        )
                    )
                    .then(Commands.literal("claim")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .executes(InvestmentRisky::investClaim)
                        )
                    )
                    .then(Commands.literal("inspect")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .executes(InvestmentRisky::investInspect)
                        )
                    )
                )
                .then(Commands.literal("loan")
                    .then(Commands.literal("take")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .then(Commands.argument("amount",IntegerArgumentType.integer())
                                //0 sporka 1 lichvar
                                .then(Commands.argument("type", IntegerArgumentType.integer())
                                    .executes(Loan::loanTake)
                                )
                            )
                        )
                    )
                    .then(Commands.literal("payoff")
                        .then(Commands.argument("player",StringArgumentType.word())
                            //-1 -> max
                            .then(Commands.argument("amount",IntegerArgumentType.integer())
                                //0 sporka 1 lichvar
                                .then(Commands.argument("type", IntegerArgumentType.integer())
                                    .executes(Loan::loanPayof)
                                )
                            )
                        )
                    )
                    .then(Commands.literal("inspect")
                        .then(Commands.argument("player",StringArgumentType.word())
                            //0 sporka 1 lichvar
                            .then(Commands.argument("type", IntegerArgumentType.integer())
                                .executes(Loan::loanInspect)
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
                    .then(Commands.literal("update")
                        .executes(ctx -> {
                            cacheStats();
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                .build()
            );
        });
    }
}
