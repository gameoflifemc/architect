package cc.architect.commands;

import cc.architect.Utilities;
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

import java.util.Objects;

import static cc.architect.leaderboards.stats.StatsCaching.cacheStats;

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
                                p.sendMessage(Component.text("Akční bod byl úspěšně využit.").color(Colors.GREEN));
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
                                .executes(ctx -> {
                                    // check player
                                    Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                    if (p == null) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    // check amount
                                    int amount = IntegerArgumentType.getInteger(ctx,"amount");
                                    if (amount <= 0) {
                                        p.sendMessage(Component.text("Nemáš dostatek emeraldů na uložení.").color(Colors.RED));
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    PlayerInventory inventory = p.getInventory();
                                    if (!inventory.contains(Material.EMERALD,amount)) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    // remove from inventory
                                    inventory.removeItemAnySlot(new ItemStack(Material.EMERALD,amount));
                                    // write to database
                                    Meta.add(p,Meta.SAVINGS,amount*10);
                                    p.sendMessage(Component.text("Úspěšně uloženo " + amount + " emeraldů. Celkem je nyní uloženo " + (Integer.parseInt(Meta.get(p,Meta.SAVINGS))/10) + " emeraldů.").color(Colors.GREEN));
                                    return Command.SINGLE_SUCCESS;
                                })
                            )
                        )
                    )
                    .then(Commands.literal("claim")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .executes(ctx -> {
                                // check player
                                Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                if (p == null) {
                                    return Command.SINGLE_SUCCESS;
                                }
                                // check amount
                                int savings = (int)Math.floor((double) Integer.parseInt(Meta.get(p, Meta.SAVINGS)) /10);
                                PlayerInventory inventory = p.getInventory();
                                if (inventory.firstEmpty() == -1) {
                                    return Command.SINGLE_SUCCESS;
                                }
                                // add to inventory
                                Utilities.addItemsToInventory(inventory, savings, Material.EMERALD);

                                // write to database
                                Meta.set(p,Meta.SAVINGS, String.valueOf(0));
                                p.sendMessage(Component.text("Úspěšně vybrány všechny emeraldy.").color(Colors.GREEN));
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    )
                    .then(Commands.literal("inspect")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .executes(ctx -> {
                                Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                if (p == null) {
                                    return Command.SINGLE_SUCCESS;
                                }
                                double savings = (double) Integer.parseInt(Meta.get(p, Meta.SAVINGS)) /10;
                                p.sendMessage(Component.text("Aktuálně máš uloženo "+savings+" emeraldů.").color(Colors.GREEN));
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    )
                )
                .then(Commands.literal("investment")
                    .then(Commands.literal("put")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .then(Commands.argument("amount",IntegerArgumentType.integer())
                                .then(Commands.argument("time",IntegerArgumentType.integer())
                                    .executes(ctx -> {
                                        // check player
                                        Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                        if (p == null) {
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        // check amount
                                        int amount = IntegerArgumentType.getInteger(ctx,"amount");
                                        int time = IntegerArgumentType.getInteger(ctx,"time");
                                        if (amount <= 0) {
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        PlayerInventory inventory = p.getInventory();
                                        if (!inventory.contains(Material.EMERALD,amount)) {
                                            p.sendMessage(Component.text("Bohužel nemáš tolik emeraldů.").color(Colors.RED));
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        int days = Integer.parseInt(Meta.get(p,Meta.DAYS));
                                        int endTime = days+time;

                                        if(endTime > 10){
                                            p.sendMessage(Component.text("Tato investice bude trvat déle než zbývajících "+(10-days)+" dnů").color(Colors.RED));
                                            return Command.SINGLE_SUCCESS;
                                        }

                                        // remove from inventory
                                        inventory.removeItemAnySlot(new ItemStack(Material.EMERALD,amount));
                                        // write to database
                                        Meta.add(p,Meta.INVESTMENTS_TOTAL,amount);

                                        String investmentsMap = Meta.get(p,Meta.INVESTMENTS_MAP);
                                        Meta.set(p,Meta.INVESTMENTS_MAP,investmentsMap + amount + "," + endTime+ ";");


                                        p.sendMessage(Component.text("Úspěšně zainvestováno " + amount + " emeraldů na dobu "+time+" dnů.").color(Colors.GREEN));
                                        printInvestments(p);
                                        return Command.SINGLE_SUCCESS;
                                    })
                                )
                            )
                        )
                    )
                    .then(Commands.literal("claim")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .executes(ctx -> {
                                // check player
                                Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                if (p == null) {
                                    return Command.SINGLE_SUCCESS;
                                }
                                PlayerInventory inventory = p.getInventory();
                                if (inventory.firstEmpty() == -1) {
                                    return Command.SINGLE_SUCCESS;
                                }

                                StringBuilder invBuilder = new StringBuilder();
                                int claimAmount = 0;
                                String investmentsMap = Meta.get(p,Meta.INVESTMENTS_MAP);
                                String[] investments = investmentsMap.split(";");
                                if(investments[0].isEmpty()) return Command.SINGLE_SUCCESS;

                                for (String investment : investments) {
                                    String[] data = investment.split(",");
                                    int amount = Integer.parseInt(data[0]);
                                    int days = Math.max(Integer.parseInt(data[1]),0);

                                    if(days>Integer.parseInt(Meta.get(p,Meta.DAYS))){
                                        invBuilder.append(amount).append(",").append(days).append(";");
                                    }else{
                                        claimAmount += amount;
                                    }
                                }

                                Meta.set(p,Meta.INVESTMENTS_MAP,invBuilder.toString());

                                // add to inventory
                                Utilities.addItemsToInventory(inventory, claimAmount, Material.EMERALD);
                                // write to database
                                p.sendMessage(Component.text("Dostal jsi " + claimAmount + " emeraldů.").color(Colors.GREEN));
                                printInvestments(p);
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    )
                    .then(Commands.literal("inspect")
                        .then(Commands.argument("player",StringArgumentType.word())
                            .executes(ctx -> {
                                Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                if (p == null) {
                                    return Command.SINGLE_SUCCESS;
                                }
                                printInvestments(p);
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    )
                )
                .then(Commands.literal("loan")
                    .then(Commands.literal("take")
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
                                        p.sendMessage(Component.text("Values more than 64 are not accepted.").color(Colors.RED));
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
                                    p.sendMessage(Component.text("Úspěšně půjčeno " + amount + " emeraldů. Nyní půjčeno celkem " + Meta.get(p,Meta.LOAN_TOTAL) + " emeraldů.").color(Colors.GREEN));
                                    return Command.SINGLE_SUCCESS;
                                })
                            )
                        )
                    )
                    .then(Commands.literal("payoff")
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
                                    p.sendMessage(Component.text("Úspěšně vráceno " + amount + " emeraldů. Nyní půjčeno celkem " + Meta.get(p,Meta.LOAN_TOTAL) + " emeraldů.").color(Colors.GREEN));
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

    public static void printInvestments(Player p){
        String investmentsMap = Meta.get(p,Meta.INVESTMENTS_MAP);
        String[] investments = investmentsMap.split(";");

        p.sendMessage(Component.text("Aktuální stav:").color(Colors.GREEN).appendNewline());

        if(investments[0].isEmpty()) {
            p.sendMessage(Component.text("Nemáš žádné aktivní investice").color(Colors.GREEN));
            return;
        }

        int playerDay = Integer.parseInt(Meta.get(p,Meta.DAYS));

        for (String investment : investments) {
            String[] data = investment.split(",");
            int amount = Integer.parseInt(data[0]);
            int days = Math.max(Integer.parseInt(data[1]) - playerDay, 0);
            p.sendMessage(Component.text("Investice s aktuální hodnotou " + amount + " emeraldů " + (days==0? "si můžes vyzvednout teď":"dostaneš za " + days + " dní.")).color(Colors.GREEN));
        }
    }
}
