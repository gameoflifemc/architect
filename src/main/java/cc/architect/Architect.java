package cc.architect;

import cc.architect.commands.Party;
import cc.architect.commands.Simulation;
import cc.architect.events.player.*;
import cc.architect.managers.RepeatingTasks;
import cc.architect.objects.Messages;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Architect extends JavaPlugin {
    public static Plugin PLUGIN;
    @Override
    public void onEnable() {
        // plugin
        PLUGIN = this;
        // config
        this.saveDefaultConfig();
        // get lifecycle manager
        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        // commands
        Simulation.register(manager);
        Party.register(manager);
        // events
        List<Listener> events = List.of(
            new InteractEntity(),
            new Join(),
            new Quit(),
            new StartSpectatingEntity(),
            new StopSpectatingEntity(),
            new ToggleSneak()
        );
        PluginManager pluginManager = this.getServer().getPluginManager();
        for (Listener event : events) {
            pluginManager.registerEvents(event,this);
        }
        // start repeating tasks
        RepeatingTasks.scheduleActionBarTask();
        // welcome message
        Bukkit.getConsoleSender().sendMessage(Messages.PLUGIN_WELCOME);
        // yay, we're up and running!
    }
}
