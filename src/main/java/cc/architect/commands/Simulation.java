package cc.architect.commands;

import cc.architect.managers.Meta;
import cc.architect.managers.Movers;
import cc.architect.managers.Routines;
import cc.architect.minigames.travel.wrapper.TravelMinigame;
import cc.architect.minigames.travel.wrapper.TravelRegistry;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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
                                    // write to database
                                    Meta.add(p,Meta.SCORE_TOTAL,IntegerArgumentType.getInteger(ctx,"amount"));
                                    return Command.SINGLE_SUCCESS;
                                })
                            )
                        )
                    )
                )
                .then(Commands.literal("savings")
                    .then(Commands.argument("put",StringArgumentType.word())
                        .then(Commands.argument("player",StringArgumentType.word())
                            .then(Commands.argument("amount",IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    // check player
                                    Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                    if (p == null) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    // check amount
                                    int amount = IntegerArgumentType.getInteger(ctx,"amount");
                                    if (amount <= 0) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    PlayerInventory inventory = p.getInventory();
                                    if (!inventory.contains(Material.EMERALD,amount)) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    // remove from inventory
                                    inventory.removeItemAnySlot(new ItemStack(Material.EMERALD,amount));
                                    // write to database
                                    Meta.add(p,Meta.SAVINGS,amount);
                                    return Command.SINGLE_SUCCESS;
                                })
                            )
                        )
                    )
                    .then(Commands.argument("claim",StringArgumentType.word())
                        .then(Commands.argument("player",StringArgumentType.word())
                            .then(Commands.argument("amount",IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    // check player
                                    Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                    if (p == null) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    // check amount
                                    int amount = IntegerArgumentType.getInteger(ctx,"amount");
                                    if (amount <= 0) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    if (amount > 64) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    if (Integer.parseInt(Meta.get(p,Meta.SAVINGS)) < amount) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    PlayerInventory inventory = p.getInventory();
                                    if (inventory.firstEmpty() == -1) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    // add to inventory
                                    inventory.addItem(new ItemStack(Material.EMERALD,amount));
                                    // write to database
                                    Meta.add(p,Meta.SAVINGS,-amount);
                                    return Command.SINGLE_SUCCESS;
                                })
                            )
                        )
                    )
                )
                .then(Commands.literal("investment")
                    .then(Commands.argument("put",StringArgumentType.word())
                        .then(Commands.argument("player",StringArgumentType.word())
                            .then(Commands.argument("amount",IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    // check player
                                    Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                    if (p == null) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    // check amount
                                    int amount = IntegerArgumentType.getInteger(ctx,"amount");
                                    if (amount <= 0) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    PlayerInventory inventory = p.getInventory();
                                    if (!inventory.contains(Material.EMERALD,amount)) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    // remove from inventory
                                    inventory.removeItemAnySlot(new ItemStack(Material.EMERALD,amount));
                                    // write to database
                                    Meta.add(p,Meta.INVESTMENTS_TOTAL,amount);
                                    return Command.SINGLE_SUCCESS;
                                })
                            )
                        )
                    )
                    .then(Commands.argument("claim",StringArgumentType.word())
                        .then(Commands.argument("player",StringArgumentType.word())
                            .then(Commands.argument("amount",IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    // check player
                                    Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                    if (p == null) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    // check amount
                                    int amount = IntegerArgumentType.getInteger(ctx,"amount");
                                    if (amount <= 0) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    if (amount > 64) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    if (Integer.parseInt(Meta.get(p,Meta.SAVINGS)) < amount) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    PlayerInventory inventory = p.getInventory();
                                    if (inventory.firstEmpty() == -1) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    // add to inventory
                                    inventory.addItem(new ItemStack(Material.EMERALD,amount));
                                    // write to database
                                    Meta.add(p,Meta.INVESTMENTS_TOTAL,-amount);
                                    return Command.SINGLE_SUCCESS;
                                })
                            )
                        )
                    )
                )
                .then(Commands.literal("loan")
                    .then(Commands.argument("take",StringArgumentType.word())
                        .then(Commands.argument("player",StringArgumentType.word())
                            .then(Commands.argument("amount",IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    // check player
                                    Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                    if (p == null) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    // check amount
                                    int amount = IntegerArgumentType.getInteger(ctx,"amount");
                                    if (amount <= 0) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    if (amount > 64) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    if (Meta.get(p,Meta.LOAN_TOTAL).equals("0")) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    PlayerInventory inventory = p.getInventory();
                                    if (inventory.firstEmpty() == -1) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    // add to inventory
                                    inventory.addItem(new ItemStack(Material.EMERALD,amount));
                                    // write to database
                                    Meta.add(p,Meta.LOAN_TOTAL,amount);
                                    return Command.SINGLE_SUCCESS;
                                })
                            )
                        )
                    )
                    .then(Commands.argument("payoff",StringArgumentType.word())
                        .then(Commands.argument("player",StringArgumentType.word())
                            .then(Commands.argument("amount",IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    // check player
                                    Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                    if (p == null) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    // check amount
                                    int amount = IntegerArgumentType.getInteger(ctx,"amount");
                                    if (amount <= 0) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    PlayerInventory inventory = p.getInventory();
                                    if (!inventory.contains(Material.EMERALD,amount)) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    // remove from inventory
                                    inventory.removeItemAnySlot(new ItemStack(Material.EMERALD,amount));
                                    // write to database
                                    Meta.add(p,Meta.LOAN_TOTAL,-amount);
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
