package cc.architect;

import cc.architect.channels.PartyChannelManager;
import cc.architect.channels.PlayerFinder;
import cc.architect.channels.ServerName;
import cc.architect.channels.TeleportChannel;
import cc.architect.commands.Party;
import cc.architect.commands.Simulation;
import cc.architect.events.player.Join;
import cc.architect.events.player.Quit;
import cc.architect.managers.Configurations;
import cc.architect.objects.Messages;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

import static cc.architect.channels.BaseChannels.PUBLIC_CHANNEL;

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
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, PUBLIC_CHANNEL);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, PUBLIC_CHANNEL, new PartyChannelManager());
        this.getServer().getMessenger().registerIncomingPluginChannel(this, PUBLIC_CHANNEL, new PlayerFinder());
        this.getServer().getMessenger().registerIncomingPluginChannel(this, PUBLIC_CHANNEL, new ServerName());
        this.getServer().getMessenger().registerIncomingPluginChannel(this, PUBLIC_CHANNEL, new TeleportChannel());
        // welcome message
        Bukkit.getConsoleSender().sendMessage(Messages.PLUGIN_WELCOME);
        Bukkit.getConsoleSender().sendMessage("Out "+ Arrays.toString(this.getServer().getMessenger().getOutgoingChannels().toArray(new String[0])));
        Bukkit.getConsoleSender().sendMessage("In "+ Arrays.toString(this.getServer().getMessenger().getIncomingChannels().toArray(new String[0])));
        // yay, we're up and running!
    }
}
