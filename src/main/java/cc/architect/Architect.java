package cc.architect;

import cc.architect.channels.*;
import cc.architect.commands.Party;
import cc.architect.commands.Simulation;
import cc.architect.events.entity.DamageByEntity;
import cc.architect.events.player.*;
import cc.architect.heads.HeadLoader;
import cc.architect.leaderboards.InitLeaderBoards;
import cc.architect.managers.Configurations;
import cc.architect.managers.Tasks;
import cc.architect.objects.Messages;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import net.kyori.adventure.util.TriState;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

public final class Architect extends JavaPlugin {
    public static Plugin PLUGIN;
    public static LuckPerms LUCKPERMS;
    public static BukkitScheduler SCHEDULER;
    @Override
    public void onEnable() {
        // plugin
        PLUGIN = this;
        // luckperms
        LUCKPERMS = LuckPermsProvider.get();
        // scheduler
        SCHEDULER = Bukkit.getScheduler();
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
            new AsyncChat(),
            new Interact(),
            new Join(),
            new Quit(),
            new BlockBreak(),
            new InteractAtEntity(),
            new DamageByEntity(),
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

        InitLeaderBoards.init();
        // welcome
        this.getComponentLogger().info(Messages.PLUGIN_WELCOME);
        // yay, we're up and running!
    }
}
