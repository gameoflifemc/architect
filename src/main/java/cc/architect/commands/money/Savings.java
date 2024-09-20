package cc.architect.commands.money;

import cc.architect.Architect;
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
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class Savings {
    public static int put(CommandContext ctx) {
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
        Meta.set(p,Meta.SAVINGS_THIS_DAY,"true");
        Meta.add(p,Meta.SAVINGS,amount*10);
        getServer().dispatchCommand(Architect.CONSOLE,"simulation score add " + p.getName() + " " + (amount*9));
        p.sendMessage(Component.text("Úspěšně uloženo " + amount + " emeraldů. Celkem je nyní uloženo " + (Integer.parseInt(Meta.get(p,Meta.SAVINGS))/10) + " emeraldů.").color(Colors.GREEN));
        return Command.SINGLE_SUCCESS;
    }

    public static int claim(CommandContext ctx) {
        Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
        if (p == null) {
            return Command.SINGLE_SUCCESS;
        }

        if (Meta.getSafe(Meta.toUser(p),Meta.SAVINGS_THIS_DAY,"false").equals("true")) {
            p.sendMessage(Component.text("Dnes jsi už uložil spoření, proto si je můžeš vyzvednout až zítra.").color(Colors.RED));
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
    }

    public static int inspect(CommandContext ctx) {
        Player p = Bukkit.getPlayerExact(StringArgumentType.getString(ctx,"player"));
        if (p == null) {
            return Command.SINGLE_SUCCESS;
        }
        double savings = (double) Integer.parseInt(Meta.get(p, Meta.SAVINGS)) /10;
        p.sendMessage(Component.text("Aktuálně máš uloženo "+savings+" emeraldů.").color(Colors.GREEN));
        return Command.SINGLE_SUCCESS;
    }

    public static void handeSavingsAdder(Player p) {
        int savings = Integer.parseInt(Meta.get(p,Meta.SAVINGS));
        Meta.add(p,Meta.SAVINGS,savings / Game.SAVINGS_DIVIDER);
    }

    public static int get(UUID p){
        return Meta.get(p,Meta.SAVINGS)==null? 0 : Integer.parseInt(Meta.get(p,Meta.SAVINGS));
    }
}
