package cc.architect;

import cc.architect.commands.Party;
import cc.architect.commands.Simulation;
import cc.architect.events.player.*;
import cc.architect.managers.PartyChannelManager;
import cc.architect.managers.Residents;
import cc.architect.managers.RepeatingTasks;
import cc.architect.objects.Messages;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public final class Architect extends JavaPlugin {
    public static Plugin PLUGIN;
    @Override
    public void onEnable() {
        // plugin
        PLUGIN = this;
        // configs
        Residents.loadConfigs();
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

        //setup channel
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, PartyChannelManager.PUBLIC_CHANNEL);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, PartyChannelManager.PUBLIC_CHANNEL, new PartyChannelManager());
        // welcome message
        Bukkit.getConsoleSender().sendMessage(Messages.PLUGIN_WELCOME);
        Bukkit.getConsoleSender().sendMessage("Out "+ Arrays.toString(this.getServer().getMessenger().getOutgoingChannels().toArray(new String[0])));
        Bukkit.getConsoleSender().sendMessage("In "+ Arrays.toString(this.getServer().getMessenger().getIncomingChannels().toArray(new String[0])));
        // yay, we're up and running!
    }
}
