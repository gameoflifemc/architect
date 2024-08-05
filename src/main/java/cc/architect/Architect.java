package cc.architect;

import cc.architect.channels.*;
import cc.architect.commands.Party;
import cc.architect.commands.Simulation;
import cc.architect.events.player.Chat;
import cc.architect.events.player.Join;
import cc.architect.events.player.Quit;
import cc.architect.managers.Configurations;
import cc.architect.objects.Messages;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

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
            new Chat(),
            new Join(),
            new Quit()
        );
        PluginManager pluginManager = this.getServer().getPluginManager();
        for (Listener event : events) {
            pluginManager.registerEvents(event,this);
        }
        // setup channels
        Messenger messenger = this.getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(this,BaseChannels.PUBLIC);
        messenger.registerIncomingPluginChannel(this,BaseChannels.PUBLIC,new PartyChannelManager());
        messenger.registerIncomingPluginChannel(this,BaseChannels.PUBLIC,new PlayerFinder());
        messenger.registerIncomingPluginChannel(this,BaseChannels.PUBLIC,new ServerName());
        messenger.registerIncomingPluginChannel(this,BaseChannels.PUBLIC,new TeleportChannel());
        messenger.registerIncomingPluginChannel(this,BaseChannels.PUBLIC,new PlayerLister());
        // welcome message
        Bukkit.getConsoleSender().sendMessage(Messages.PLUGIN_WELCOME);
        // yay, we're up and running!
    }
    @Override
    public void onDisable() {
        // send players to limbo to prevent error messages
        this.getServer().getOnlinePlayers().forEach(p -> {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF(BaseChannels.CONNECT);
            out.writeUTF(BaseChannels.LIMBO);
            p.sendPluginMessage(this,BaseChannels.PUBLIC,out.toByteArray());
        });
    }
}
