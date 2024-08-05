package cc.architect;

import cc.architect.channels.*;
import cc.architect.commands.Party;
import cc.architect.commands.Simulation;
import cc.architect.events.player.Join;
import cc.architect.events.player.Quit;
import cc.architect.managers.Configurations;
import cc.architect.objects.Messages;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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

        // load configurations
        Configurations.load();
        // get lifecycle manager
        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        // commands
        Simulation.register(manager);
        Party.register(manager);
        // events
        List<Listener> events = List.of(
            new Join(),
            new Quit()
        );
        PluginManager pluginManager = this.getServer().getPluginManager();
        for (Listener event : events) {
            pluginManager.registerEvents(event,this);
        }
        // setup channels
        this.getServer().getMessenger().registerOutgoingPluginChannel(this,BaseChannels.PUBLIC);
        this.getServer().getMessenger().registerIncomingPluginChannel(this,BaseChannels.PUBLIC,new PartyChannelManager());
        this.getServer().getMessenger().registerIncomingPluginChannel(this,BaseChannels.PUBLIC,new PlayerFinder());
        this.getServer().getMessenger().registerIncomingPluginChannel(this,BaseChannels.PUBLIC,new ServerName());
        this.getServer().getMessenger().registerIncomingPluginChannel(this,BaseChannels.PUBLIC,new TeleportChannel());
        this.getServer().getMessenger().registerIncomingPluginChannel(this,BaseChannels.PUBLIC,new PlayerLister());
        // welcome message
        Bukkit.getConsoleSender().sendMessage(Messages.PLUGIN_WELCOME);
        // yay, we're up and running!
    }
}
