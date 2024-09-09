package cc.architect.commands;

import cc.architect.Architect;
import cc.architect.Utilities;
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
                                    getServer().dispatchCommand(Architect.CONSOLE,"simulation score add " + p.getName() + " " + (amount*9));
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
                                if(investments[0].isEmpty()) {
                                    p.sendMessage(Component.text("Nemáš žádné aktivní investice.").color(Colors.RED));
                                    return Command.SINGLE_SUCCESS;
                                }

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
                                //0 sporka 1 lichvar
                                .then(Commands.argument("type", IntegerArgumentType.integer())
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
                                        int type = IntegerArgumentType.getInteger(ctx,"type");
                                        switch (type) {
                                            case 0:
                                                sporkaTake(p, amount);
                                                break;
                                            case 1:
                                                lichvarTake(p, amount);
                                                break;
                                        }
                                        return Command.SINGLE_SUCCESS;
                                    })
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
                                    .executes(ctx -> {
                                        // check player
                                        Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                        if (p == null) {
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        // check amount
                                        int amount = IntegerArgumentType.getInteger(ctx,"amount");
                                        int type = IntegerArgumentType.getInteger(ctx,"type");
                                        if(type==1){
                                            lichvarPayoff(p,amount);
                                        }else{
                                            sporPayoff(p,amount);
                                        }
                                        /*PlayerInventory inventory = p.getInventory();
                                        if (!inventory.contains(Material.EMERALD,amount)) {
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        // remove from inventory
                                        inventory.removeItemAnySlot(new ItemStack(Material.EMERALD,amount));
                                        // write to database
                                        p.sendMessage(Component.text("Úspěšně vráceno " + amount + " emeraldů. Nyní půjčeno celkem " + Meta.get(p,Meta.LOAN_TOTAL) + " emeraldů.").color(Colors.GREEN));*/
                                        return Command.SINGLE_SUCCESS;
                                    })
                                )
                            )
                        )
                    )
                    .then(Commands.literal("inspect")
                        .then(Commands.argument("player",StringArgumentType.word())
                            //0 sporka 1 lichvar
                            .then(Commands.argument("type", IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
                                    if (p == null) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    // check amount
                                    int amount = IntegerArgumentType.getInteger(ctx,"type");
                                    switch (amount) {
                                        case 0:
                                            p.sendMessage(Component.text("Aktuálně máš půjčku s hodnotou " + (int)Math.ceil((double) Integer.parseInt(Meta.get(p, Meta.LOAN_SAFE)) /10) + " emeraldů.").color(Colors.GREEN));
                                            break;
                                        case 1:
                                            printLichvar(p);
                                            break;
                                    }
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

    public static void printLichvar(Player p){
        String lichMap = Meta.get(p,Meta.LOAN_RISKY_MAP);
        String[] loans = lichMap.split(";");

        p.sendMessage(Component.text("Aktuální stav:").color(Colors.GREEN).appendNewline());

        if(loans[0].isEmpty()) {
            p.sendMessage(Component.text("Nemáš žádné aktivní půjčky u Vladislava Sudého").color(Colors.GREEN));
            return;
        }

        for (String loan : loans) {
            String[] data = loan.split(",");
            int amount = Integer.parseInt(data[0]);
            int percent = Integer.parseInt(data[1]);
            p.sendMessage(Component.text("Půjčka s aktuální hodnotou " + amount + " emeraldů a úrokem "+percent+".").color(Colors.RED));
        }
    }
    public static void sporkaTake(Player p, int amount){
        if(Meta.get(p,Meta.LOAN_SAFE_HAD_LOAN).equals("true")){
            p.sendMessage(Component.text("Tento den jsi si u už spořitelny půjčil, přijdi další den aby jsi mohl vytvořit novou půjčku.").color(Colors.RED));
            return;
        }
        String map = Meta.get(p,Meta.LOAN_SAFE);

        if(!(Meta.get(p,Meta.LOAN_RISKY_MAP).isEmpty()&&map.equals("0"))) {
            if(!map.equals("0")){
                p.sendMessage(Component.text("Už u nás máš vytořenou půjčku, nejdříve ji raději zplať aby jsi nezbankrotoval.").color(Colors.RED));
            }else{
                p.sendMessage(Component.text("Vidíme že máš vytvořenou půjčku, ale ne u nás. Doporučujeme ti ji co nejdříve zaplatit než se ti nasčítají úroky.").color(Colors.RED));
            }
            return;
        }

        Meta.set(p,Meta.LOAN_SAFE, String.valueOf((int)(amount*10+(amount*10)*Game.LOAN_SPOR_INSTANT)));

        Utilities.addItemsToInventory(p.getInventory(), amount, Material.EMERALD);

        p.sendMessage(Component.text("Úspěšně jsi vytvořil půjčku s hodnotou "+amount+".").color(Colors.GREEN));

        Meta.add(p,Meta.LOAN_TOTAL,amount);
    }
    public static void lichvarTake(Player p, int amount){
        String lichMap = Meta.get(p,Meta.LOAN_RISKY_MAP);

        int lich;
        try {
            lich = Integer.parseInt(Meta.get(p, LOAN_RISKY_COUNTER));
        } catch (NumberFormatException e) {
            lich = 10;
            Meta.set(p, LOAN_RISKY_COUNTER,"10");
        }

        String[] loans = lichMap.split(";");

        if(loans.length>3) {
            p.sendMessage(Component.text("Máš u mě už moc půjček.").color(Colors.RED));
            return;
        }

        Meta.set(p,Meta.LOAN_RISKY_MAP,lichMap+ (amount+lich)+","+lich+";");

        Utilities.addItemsToInventory(p.getInventory(), amount, Material.EMERALD);

        p.sendMessage(Component.text("Úspěšně jsi vytvořil půjčku s hodnotou "+amount+" emeraldů a úrokem "+lich+" emeraldů.").color(Colors.GREEN));

        if(lich==9) return;
        Meta.add(p,Meta.LOAN_TOTAL,amount);
        Meta.add(p, LOAN_RISKY_COUNTER,1);
    }

    public static void lichvarPayoff(Player p,int amount) {
        if(amount==-1){
            int removed = 0;
            AtomicInteger emeraldsInventory = new AtomicInteger();
            p.getInventory().forEach(itemStack -> {
                if(itemStack!=null&&itemStack.getType()==Material.EMERALD){
                    emeraldsInventory.addAndGet(itemStack.getAmount());
                }
            });

            String lichMap = Meta.get(p,Meta.LOAN_RISKY_MAP);
            String[] loans = lichMap.split(";");

            if(loans[0].isEmpty()) {
                p.sendMessage(Component.text("Nemáš žádné aktivní půjčky u Vladislava Sudého").color(Colors.RED));
                return;
            }

            StringBuilder loanBuilder = new StringBuilder();

            for (String loan : loans) {
                String[] data = loan.split(",");
                int loanAmount = Integer.parseInt(data[0]);

                if(removed+loanAmount<=emeraldsInventory.get()) {
                    removed += loanAmount;
                    //TODO scoreeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
                    Meta.add(p,Meta.SCORE_TOTAL,10);
                }else {
                    loanBuilder.append(loanAmount-(emeraldsInventory.get()-removed)).append(",").append(data[1]).append(";");
                    removed += emeraldsInventory.get()-removed;
                    break;
                }
            }

            Meta.set(p,Meta.LOAN_RISKY_MAP,loanBuilder.toString());

            p.getInventory().removeItemAnySlot(new ItemStack(Material.EMERALD,removed));

            p.sendMessage(Component.text("Úspěšně jsi vrátil "+removed+" emeraldů.").color(Colors.GREEN));
            printLichvar(p);
        }else {

            if(!p.getInventory().contains(Material.EMERALD,amount)){
                p.sendMessage(Component.text("Nemáš tento počet emeraldů.").color(Colors.RED));
                return;
            }
            int removed = 0;

            String lichMap = Meta.get(p,Meta.LOAN_RISKY_MAP);
            String[] loans = lichMap.split(";");

            if(loans[0].isEmpty()) {
                p.sendMessage(Component.text("Nemáš žádné aktivní půjčky u Vladislava Sudého").color(Colors.RED));
                return;
            }

            StringBuilder loanBuilder = new StringBuilder();

            for (String loan : loans) {
                String[] data = loan.split(",");
                int loanAmount = Integer.parseInt(data[0]);

                if(removed+loanAmount<=amount) {
                    removed += loanAmount;
                    //TODO scoreeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
                    Meta.add(p,Meta.SCORE_TOTAL,10);
                }else {
                    loanBuilder.append(loanAmount-(amount-removed)).append(",").append(data[1]).append(";");
                    removed += amount-removed;
                    break;
                }
            }

            Meta.set(p,Meta.LOAN_RISKY_MAP,loanBuilder.toString());

            p.getInventory().removeItemAnySlot(new ItemStack(Material.EMERALD,removed));

            p.sendMessage(Component.text("Úspěšně jsi vrátil "+removed+" emeraldů.").color(Colors.GREEN));

            printLichvar(p);
        }
    }

    public static void sporPayoff(Player p,int amount) {
        if(amount==-1){
            int removed = 0;
            AtomicInteger emeraldsInventory = new AtomicInteger();
            p.getInventory().forEach(itemStack -> {
                if(itemStack!=null&&itemStack.getType()==Material.EMERALD){
                    emeraldsInventory.addAndGet(itemStack.getAmount());
                }
            });

            int map = (int) Math.ceil((double) Integer.parseInt(Meta.get(p, Meta.LOAN_SAFE)) /10);

            if(map<=0){
                p.sendMessage(Component.text("Aktuálně nemáš žádné půjčky.").color(Colors.RED));
            }

            if(map<=emeraldsInventory.get()) {
                removed += map;
                Meta.set(p,Meta.LOAN_SAFE, String.valueOf(0));
                //TODO scoreeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
                Meta.add(p,Meta.SCORE_TOTAL,100);
            }else {
                removed += emeraldsInventory.get();
                Meta.set(p,Meta.LOAN_SAFE, String.valueOf(map-emeraldsInventory.get()));
            }

            p.getInventory().removeItemAnySlot(new ItemStack(Material.EMERALD,removed));

            p.sendMessage(Component.text("Úspěšně jsi vrátil "+removed+" emeraldů.").color(Colors.GREEN));
            p.sendMessage(Component.text("Aktuálně máš půjčku s hodnotou " + (int)Math.ceil((double) Integer.parseInt(Meta.get(p, Meta.LOAN_SAFE)) /10) + " emeraldů.").color(Colors.GREEN));
        }else {
            if(!p.getInventory().contains(Material.EMERALD,amount)){
                p.sendMessage(Component.text("Nemáš tento počet emeraldů.").color(Colors.RED));
                return;
            }
            int removed = 0;

            int map = (int) Math.ceil((double) Integer.parseInt(Meta.get(p, Meta.LOAN_SAFE)) /10);

            if(map<=0){
                p.sendMessage(Component.text("Aktuálně nemáš žádné půjčky.").color(Colors.RED));
            }

            if(map<=amount) {
                removed += map;
                Meta.set(p,Meta.LOAN_SAFE, String.valueOf(0));
                //TODO scoreeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
                Meta.add(p,Meta.SCORE_TOTAL,100);
            }else {
                removed += amount;
                Meta.set(p,Meta.LOAN_SAFE, String.valueOf(map-amount));
            }

            p.getInventory().removeItemAnySlot(new ItemStack(Material.EMERALD,removed));

            p.sendMessage(Component.text("Úspěšně jsi vrátil "+removed+" emeraldů.").color(Colors.GREEN));
            p.sendMessage(Component.text("Aktuálně máš půjčku s hodnotou " + (int)Math.ceil((double) Integer.parseInt(Meta.get(p, Meta.LOAN_SAFE)) /10) + " emeraldů.").color(Colors.GREEN));
        }
    }
}
