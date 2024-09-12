package cc.architect.commands.money;

import cc.architect.Utilities;
import cc.architect.managers.Game;
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

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static cc.architect.managers.Meta.LOAN_RISKY_COUNTER;

public class Loan {
    public static int loanTake(CommandContext ctx) {
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
    }

    public static int loanPayof(CommandContext ctx) {
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

        return Command.SINGLE_SUCCESS;
    }

    public static int loanInspect(CommandContext ctx) {
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
    }

    public static void handleLoanLichvarAdd(Player p) {
        StringBuilder loanBuilder = new StringBuilder();
        String loansMap = Meta.get(p,Meta.LOAN_RISKY_MAP);
        String[] loans = loansMap.split(";");
        if(loans[0].isEmpty()) return;

        for (String investment : loans) {
            String[] data = investment.split(",");
            int amount = Integer.parseInt(data[0]);
            int adder = Integer.parseInt(data[1]);

            int newAmount = amount + adder;
            loanBuilder.append(newAmount).append(",").append(adder).append(";");
        }

        Meta.set(p,Meta.LOAN_RISKY_MAP,loanBuilder.toString());
    }
    public static void handleLoanSporAdd(Player p) {
        int loan_spor = Integer.parseInt(Meta.get(p,Meta.LOAN_SAFE));
        Meta.add(p,Meta.LOAN_SAFE, (int) (loan_spor * Game.LOAN_SPOR_INSTANT));
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

    public static void sporkaTake(Player p, int amount){
        if(Meta.get(p,Meta.LOAN_SAFE_HAD_LOAN).equals("true")){
            p.sendMessage(Component.text("Tento den jsi si u už spořitelny půjčil, přijdi další den aby jsi mohl vytvořit novou půjčku.").color(Colors.RED));
            return;
        }
        String map = Meta.get(p,Meta.LOAN_SAFE);

        if(!(map.equals("0"))) {
            p.sendMessage(Component.text("Už u nás máš vytořenou půjčku, nejdříve ji raději zplať aby jsi nezbankrotoval.").color(Colors.RED));
            return;
        }

        Meta.set(p,Meta.LOAN_SAFE, String.valueOf((int)(amount*10+(amount*10)* Game.LOAN_SPOR_INSTANT)));

        Utilities.addItemsToInventory(p.getInventory(), amount, Material.EMERALD);

        p.sendMessage(Component.text("Úspěšně jsi vytvořil půjčku s hodnotou "+amount+".").color(Colors.GREEN));

        Meta.add(p,Meta.LOAN_TOTAL,amount);
        Meta.set(p,Meta.LOAN_SAFE_HAD_LOAN,"true");
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

        }
        printLichvar(p);
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

    public static int countAllAmount(UUID p){
        String lichMap = Meta.get(p,Meta.LOAN_RISKY_MAP);
        if(lichMap.isEmpty()) return 0;
        String[] loans = lichMap.split(";");
        if(loans[0].isEmpty()) return 0;
        int amount = 0;
        for (String loan : loans) {
            String[] data = loan.split(",");
            amount += Integer.parseInt(data[0]);
        }
        amount += (int) Math.ceil(Integer.parseInt(Meta.get(p,Meta.LOAN_SAFE))/10.0);
        return amount;
    }
}
