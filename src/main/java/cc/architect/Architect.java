package cc.architect;

import cc.architect.channels.Teleport;
import cc.architect.channels.*;
import cc.architect.commands.Discord;
import cc.architect.commands.Simulation;
import cc.architect.commands.Unstuck;
import cc.architect.events.block.Break;
import cc.architect.events.block.Place;
import cc.architect.events.entity.Damage;
import cc.architect.events.entity.DamageByEntity;
import cc.architect.events.entity.RemoveFromWorld;
import cc.architect.events.misc.AsyncChat;
import cc.architect.events.misc.FoodLevelChange;
import cc.architect.events.player.*;
import cc.architect.leaderboards.InitLeaderBoards;
import cc.architect.managers.Compasses;
import cc.architect.managers.Items;
import cc.architect.managers.Tasks;
import cc.architect.minigames.travel.wrapper.TravelRegistry;
import cc.architect.objects.Messages;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import net.kyori.adventure.util.TriState;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.entity.Entity;

import java.util.List;

public final class Architect extends JavaPlugin {
    public static Plugin PLUGIN;
    public static LuckPerms LUCKPERMS;
    public static BukkitScheduler SCHEDULER;
    public static ConsoleCommandSender CONSOLE;
    public static World WORLD;
    public static World MINE;
    public static World FARM;
    @Override
    public void onEnable() {
        // plugin
        PLUGIN = this;
        // luckperms
        LUCKPERMS = LuckPermsProvider.get();
        // scheduler
        SCHEDULER = Bukkit.getScheduler();
        // console
        CONSOLE = Bukkit.getConsoleSender();
        // world
        WORLD = Bukkit.getWorld("world");
        // commands
        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        Discord.register(manager);
        cc.architect.commands.Party.register(manager);
        Simulation.register(manager);
        Unstuck.register(manager);
        // events
        List<Listener> events = List.of(
            // block
            new Break(),
            new Place(),
            // entity
            new Damage(),
            new DamageByEntity(),
            new cc.architect.events.entity.Death(),
            new RemoveFromWorld(),
            // misc
            new AsyncChat(),
            new FoodLevelChange(),
            // player
            new ChangedWorld(),
            new Death(),
            new DropItem(),
            new Interact(),
            new InteractAtEntity(),
            new Join(),
            new Move(),
            new Quit(),
            new Respawn(),
            new SpawnLocation(),
            new cc.architect.events.player.Teleport()
        );
        PluginManager pluginManager = this.getServer().getPluginManager();
        for (Listener event : events) {
            pluginManager.registerEvents(event,this);
        }
        // channels
        Messenger messenger = this.getServer().getMessenger();
        List<PluginMessageListener> channels = List.of(
            new Party(),
            new PlayerFinder(),
            new PlayerLister(),
            new ServerName(),
            new Teleport()
        );
        for (PluginMessageListener channel : channels) {
            messenger.registerIncomingPluginChannel(this,Base.PUBLIC,channel);
        }
        messenger.registerOutgoingPluginChannel(this,Base.PUBLIC);
        // worlds
        List<String> worlds = List.of(
            "village",
            "mine",
            "farm",
            "travel"
        );
        for (String worldName : worlds) {
            World world = new WorldCreator(worldName).keepSpawnLoaded(TriState.FALSE).createWorld();
            switch (worldName) {
                case "mine":
                    MINE = world;
                    break;
                case "farm":
                    FARM = world;
                    break;
            }
        }
        // tasks
        Tasks.registerTasks();
        // items
        Items.loadAll();
        // compasses
        Compasses.setupValues();
        // leaderboards
        InitLeaderBoards.init();
        // travel mini-games
        TravelRegistry.init();
        // welcome
        this.getComponentLogger().info(Messages.PLUGIN_WELCOME);
        // yay, we're up and running!
    }

    @Override
    public void onDisable() {
        TravelRegistry.entities.values().forEach(entity -> {((LivingEntity)entity).setHealth(0);});
    }
}
