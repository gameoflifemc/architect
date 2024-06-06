package cc.architect.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cc.architect.managers.Dialogue.enterDialogue;
import static cc.architect.managers.Dialogue.leaveDialogue;
import static cc.architect.managers.Instance.initializeSimulation;
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
            case "enterdialogue" -> {
                Bukkit.broadcast(Component.text("Entering dialogue..."));
                Entity target = p.getTargetEntity(5,false);
                if (target == null) {
                    return;
                }
                Location loc;
                if (target instanceof Player) {
                    loc = ((Player) target).getEyeLocation();
                } else {
                    loc = target.getLocation();
                }
                enterDialogue(p, loc);
            }
            case "leavedialogue" -> {
                Bukkit.broadcast(Component.text("Exiting dialogue..."));
                leaveDialogue(p);
            }
        }
    }
}
