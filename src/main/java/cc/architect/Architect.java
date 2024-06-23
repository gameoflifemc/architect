package cc.architect;

import cc.architect.commands.Simulation;
import cc.architect.events.player.*;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static cc.architect.managers.RepeatingTasks.scheduleActionBarTask;
import static org.bukkit.Bukkit.getConsoleSender;

public final class Architect extends JavaPlugin {
    public static Plugin plugin;
    @Override
    public void onEnable() {
        // plugin
        plugin = this;
        // config
        saveDefaultConfig();
        // commands
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,event -> {
            final Commands commands = event.registrar();
            commands.register("simulation",new Simulation());
        });
        // events
        List<Listener> events = List.of(
            new InteractEntity(),
            new Join(),
            new Quit(),
            new StartSpectatingEntity(),
            new StopSpectatingEntity(),
            new ToggleSneak()
        );
        PluginManager pluginManager = getServer().getPluginManager();
        for (Listener event : events) {
            pluginManager.registerEvents(event,this);
        }
        // start repeating tasks
        scheduleActionBarTask();
        // welcome message
        getConsoleSender().sendMessage(Component.text("Core Architect protocol running, the Game of Life is ready to begin.")
            .color(TextColor.fromHexString("#FF1313")));
        // yay, we're up and running!
    }
}
