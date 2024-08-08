package cc.architect;

import cc.architect.channels.*;
import cc.architect.commands.Party;
import cc.architect.commands.Simulation;
import cc.architect.events.entity.PlayerHurtEntity;
import cc.architect.events.player.*;
import cc.architect.heads.HeadLoader;
import cc.architect.managers.Configurations;
import cc.architect.objects.Messages;
import cc.architect.managers.Tasks;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import net.kyori.adventure.util.TriState;
import org.bukkit.WorldCreator;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.List;

public final class Architect extends JavaPlugin {
    public static Plugin PLUGIN;
    @Override
    public void onEnable() {
        // plugin
        PLUGIN = this;
        // configurations
        Configurations.load();
        // commands
        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        Simulation.register(manager);
        Party.register(manager);
        // heads
        HeadLoader.load();
        // tasks
        Tasks.registerTasks();
        // events
        List<Listener> events = List.of(
            new Chat(),
            new Interact(),
            new Join(),
            new Quit(),
            new BlockBreak(),
            new PlayerEntityInteraction(),
            new PlayerHurtEntity(),
            new SpawnLocation()
        );
        PluginManager pluginManager = this.getServer().getPluginManager();
        for (Listener event : events) {
            pluginManager.registerEvents(event,this);
        }
        // channels
        Messenger messenger = this.getServer().getMessenger();
        List<PluginMessageListener> channels = List.of(
            new PartyChannelManager(),
            new PlayerFinder(),
            new ServerName(),
            new TeleportChannel(),
            new PlayerLister()
        );
        for (PluginMessageListener channel : channels) {
            messenger.registerIncomingPluginChannel(this,BaseChannels.PUBLIC,channel);
        }
        messenger.registerOutgoingPluginChannel(this,BaseChannels.PUBLIC);
        // worlds
        List<String> worlds = List.of(
            "village",
            "mine",
            "farm",
            "dream",
            "buildings"
        );
        for (String world : worlds) {
            new WorldCreator(world).keepSpawnLoaded(TriState.FALSE).createWorld();
        }
        // welcome
        this.getComponentLogger().info(Messages.PLUGIN_WELCOME);
        // yay, we're up and running!
    }
    @Override
    public void onDisable() {
        // send players to limbo to prevent error disconnect messages
        this.getServer().getOnlinePlayers().forEach(p -> {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF(BaseChannels.CONNECT);
            out.writeUTF(BaseChannels.LIMBO);
            p.sendPluginMessage(this,BaseChannels.PUBLIC,out.toByteArray());
        });
        // ...and we're done!
    }
}
