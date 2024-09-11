package cc.architect.commands.money;

import cc.architect.managers.Meta;
import cc.architect.objects.Colors;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.codehaus.plexus.util.cli.Commandline;

public class InvenstmentBasic {
    public static ArgumentBuilder register() {
        return Commands.literal("investment")
                .then(Commands.literal("put")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
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
                );
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
