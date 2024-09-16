package cc.architect;

import cc.architect.channels.*;
import cc.architect.commands.*;
import cc.architect.events.block.Break;
import cc.architect.events.entity.Damage;
import cc.architect.events.entity.DamageByEntity;
import cc.architect.events.misc.BlockGrow;
import cc.architect.events.misc.FoodLevelChange;
import cc.architect.events.player.*;
import cc.architect.leaderboards.InitLeaderBoards;
import cc.architect.managers.Compasses;
import cc.architect.managers.Facts;
import cc.architect.managers.Items;
import cc.architect.managers.Tasks;
import cc.architect.minigames.travel.wrapper.TravelRegistry;
import cc.architect.objects.Messages;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
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

import java.util.List;
import java.util.UUID;

public final class Architect extends JavaPlugin {
    public static Plugin PLUGIN;
    public static LuckPerms LUCKPERMS;
    public static BukkitScheduler SCHEDULER;
    public static ConsoleCommandSender CONSOLE;
    public static World WORLD;
    public static String SESSION;
    public static ComponentLogger LOGGER;
    public static World VILLAGE;
    public static World MINE;
    public static World FARM;
    public static World TRAVEL;
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
        // session id
        SESSION = UUID.randomUUID().toString();
        // logger
        LOGGER = this.getComponentLogger();
        // commands
        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        Code.register(manager);
        Discord.register(manager);
        Invite.register(manager);
        Simulation.register(manager);
        Unstuck.register(manager);
        // events
        List<Listener> events = List.of(
            // block
            new Break(),
            // entity
            new Damage(),
            new DamageByEntity(),
            new cc.architect.events.entity.Death(),
            // misc
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
            new Teleport(),
            new BlockGrow()
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
            new ServerName()
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
                case "village":
                    VILLAGE = world;
                    break;
                case "mine":
                    MINE = world;
                    break;
                case "farm":
                    FARM = world;
                    break;
                case "travel":
                    TRAVEL = world;
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
        // facts
        Facts.initialize();
        // travel mini-games
        TravelRegistry.init();
        // welcome
        LOGGER.info(Messages.PLUGIN_WELCOME);
        // yay, we're up and running!
    }
    @Override
    public void onDisable() {
        TravelRegistry.entities.values().forEach(entity -> ((LivingEntity) entity).setHealth(0));
    }
}
