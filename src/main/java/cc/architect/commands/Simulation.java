package cc.architect.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getPlayer;

public class Simulation implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (args.length != 2) {
            return;
        }
        if (stack.getSender() instanceof Player) {
            return;
        }
        Player player = getPlayer(args[1]);
        if (player == null) {
            return;
        }
        switch (args[0]) {
            case "initialize" -> {
                Bukkit.broadcast(Component.text("Creating simulation..."));
                Bukkit.broadcast(Component.text("Creating simulation..."));
            }
            case "assimilate" -> {
                Bukkit.broadcast(Component.text("Assimilating simulation..."));
                Bukkit.broadcast(Component.text("Assimilating simulation..."));
            }
        }
    }
}
