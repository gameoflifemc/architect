package cc.architect;

import cc.architect.commands.Simulation;
import cc.architect.events.player.*;
import cc.architect.managers.RepeatingTasks;
import cc.architect.objects.Messages;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
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
        // commands
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,event -> event.registrar().register("simulation",new Simulation()));
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
        RepeatingTasks.scheduleActionBarTask();
        // welcome message
        Bukkit.getConsoleSender().sendMessage(Messages.WELCOME);
        // yay, we're up and running!
    }
}
