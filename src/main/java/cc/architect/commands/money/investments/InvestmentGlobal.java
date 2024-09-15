package cc.architect.commands.money.investments;

import cc.architect.Utilities;
import cc.architect.managers.Meta;
import cc.architect.objects.Colors;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class InvestmentGlobal {
    public static int investPut(CommandContext ctx, String metaKey) {
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

        String investmentsMap = Meta.get(p,metaKey);
        Meta.set(p,metaKey,investmentsMap + amount + "," + endTime+ ";");


        p.sendMessage(Component.text("Úspěšně zainvestováno " + amount + " emeraldů na dobu "+time+" dnů.").color(Colors.GREEN));
        printInvestments(p,metaKey);

        return Command.SINGLE_SUCCESS;
    }

    public static int investClaim(CommandContext ctx, String metaKey) {
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
        String investmentsMap = Meta.get(p,metaKey);
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

        Meta.set(p,metaKey,invBuilder.toString());

        // add to inventory
        Utilities.addItemsToInventory(inventory, claimAmount, Material.EMERALD);
        // write to database
        p.sendMessage(Component.text("Dostal jsi " + claimAmount + " emeraldů.").color(Colors.GREEN));
        printInvestments(p,metaKey);
        return Command.SINGLE_SUCCESS;
    }

    public static int investInspect(CommandContext ctx, String metaKey) {
        Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
        if (p == null) {
            return Command.SINGLE_SUCCESS;
        }
        printInvestments(p,metaKey);
        return Command.SINGLE_SUCCESS;
    }

    public static void printInvestments(Player p, String metaKey) {
        String investmentsMap = Meta.get(p,metaKey);
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

    public static int countAllInvestments(UUID p) {
        return countInvestment(p,Meta.INVESTMENTS_MAP)+countInvestment(p,Meta.INVESTMENTS_MAP_RISKY);
    }

    public static int countInvestment(UUID p, String metaKey) {
        int investments = 0;
        String investmentsMap = Meta.get(p,metaKey);
        if(investmentsMap != null) {
            String[] investmentsArray = investmentsMap.split(";");
            if(!investmentsArray[0].isEmpty()) {
                for (String investment : investmentsArray) {
                    String[] data = investment.split(",");
                    investments += Integer.parseInt(data[0]);
                }
            }
        }

        return investments;
    }

}
