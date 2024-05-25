package cc.architect;

import cc.architect.commands.Simulation;
import cc.architect.events.Join;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static org.bukkit.Bukkit.getConsoleSender;

public final class Architect extends JavaPlugin {
    @Override
    public void onEnable() {
        // commands
        List<String> commandNames = List.of("simulation");
        List<CommandExecutor> commandClasses = List.of(new Simulation());
        for(int i = 0; i < commandNames.size(); i++) {
            PluginCommand pluginCommand = getCommand(commandNames.get(i));
            if (pluginCommand == null) {
                return;
            }
            pluginCommand.setExecutor(commandClasses.get(i));
        }
        // events
        List<Listener> events = List.of(new Join());
        for (Listener event : events) {
            getServer().getPluginManager().registerEvents(event,this);
        }
        // welcome message
        getConsoleSender().sendMessage(Component.text("Core Architect protocol running, the Game of Life is ready to begin.")
            .color(TextColor.fromHexString("#FF1313")));
        // yay, we're up and running!
    }
}