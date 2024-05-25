package cc.architect.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cc.architect.Utilities.initializeSimulation;

public class Simulation implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length != 2) {
            return false;
        }
        switch (args[0]) {
            case "initialize" -> {
                Bukkit.broadcast(Component.text("Creating simulation..."));
                initializeSimulation(args[1]);
                return true;
            }
            case "assimilate" -> {
                Bukkit.broadcast(Component.text("Assimilating simulation..."));
                return true;
            }
        }
        return false;
    }
}
