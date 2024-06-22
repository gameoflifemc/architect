package cc.architect.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cc.architect.managers.Instances.initializeSimulation;
import static org.bukkit.Bukkit.getPlayer;

public class Simulation implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (args.length != 2) {
            return;
        }
        Player p = getPlayer(args[1]);
        if (p == null) {
            return;
        }
        switch (args[0]) {
            case "initialize" -> {
                Bukkit.broadcast(Component.text("Creating simulation..."));
                initializeSimulation(p.getName());
            }
            case "assimilate" -> {
                Bukkit.broadcast(Component.text("Assimilating simulation..."));
                Bukkit.broadcast(Component.text("Assimilating simulation..."));
            }
        }
    }
}
