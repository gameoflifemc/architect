package cc.architect.commands;

import cc.architect.managers.Instances;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Simulation implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (args.length != 3) {
            return;
        }
        Player p = Bukkit.getPlayer(args[1]);
        if (p == null) {
            return;
        }
        switch (args[0]) {
            case "initialize" -> {
                Bukkit.broadcast(Component.text("Initializing simulation..."));
                Instances.initialize(p.getName(),args[2]);
            }
            case "assimilate" -> {
                Bukkit.broadcast(Component.text("Assimilating player to simulation..."));
                Instances.assimilate(p);
            }
            case "decommission" -> {
                Bukkit.broadcast(Component.text("Decommissioning simulation..."));
                Instances.decommission(p.getName());
            }
        }
    }
}
