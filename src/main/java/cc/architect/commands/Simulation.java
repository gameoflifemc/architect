package cc.architect.commands;

import cc.architect.managers.Configurations;
import cc.architect.managers.Instances;
import cc.architect.objects.Messages;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

import static cc.architect.leaderboards.PlayerStatsBoard.createStatsLeaderBoard;

public class Simulation {
    public static void register(LifecycleEventManager<Plugin> manager) {
        // create and register command
        manager.registerEventHandler(LifecycleEvents.COMMANDS,event -> {
            // register command
            event.registrar().register(Commands.literal("simulation")
                //creates a new world from a template for player
                .then(Commands.literal("initialize")
                    .then(Commands.argument("player",StringArgumentType.word())
                        .then(Commands.argument("template",StringArgumentType.word())
                            .executes(ctx -> {
                                Player p = Bukkit.getPlayer(StringArgumentType.getString(ctx,"player"));
                                if (p == null) {
                                    return Command.SINGLE_SUCCESS;
                                }
                                p.sendMessage(Messages.INSTANCE_INITIALIZING);
                                Instances.initialize(p,StringArgumentType.getString(ctx,"template"));
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    )
                )
                //teleports player to world spawn of its world
                .then(Commands.literal("assimilate")
                    .then(Commands.argument("player",StringArgumentType.word())
                        .executes(ctx -> {
                            Player p = Bukkit.getPlayer(StringArgumentType.getString(ctx,"player"));
                            if (p == null) {
                                return Command.SINGLE_SUCCESS;
                            }
                            Bukkit.broadcast(Messages.INSTANCE_ASSIMILATING);
                            Instances.assimilate(p);
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )
                //deletes player's world
                //TODO: SUS
                .then(Commands.literal("decommission")
                    .then(Commands.argument("player",StringArgumentType.word())
                        .executes(ctx -> {
                            Player p = Bukkit.getPlayer(StringArgumentType.getString(ctx,"player"));
                            if (p == null) {
                                return Command.SINGLE_SUCCESS;
                            }
                            Bukkit.broadcast(Messages.INSTANCE_DECOMMISSIONING);
                            Instances.decommission(p);
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )
                .then(Commands.literal("interaction")
                    .then(Commands.argument("dialogue",StringArgumentType.word())
                        .executes(ctx -> {
                            // get sender
                            Player p = Bukkit.getPlayer(ctx.getSource().getSender().getName());
                            if (p == null) {
                                return Command.SINGLE_SUCCESS;
                            }
                            // get target
                            Location target = p.getTargetBlock(null,5).getLocation().add(0.5,1.0,0.5);
                            // get world
                            World world = p.getWorld();
                            // create outer interaction entity
                            Interaction outer = (Interaction) world.spawnEntity(target,EntityType.INTERACTION);
                            // create inner interaction entity
                            Interaction inner = (Interaction) world.spawnEntity(target,EntityType.INTERACTION);
                            // get dialogue id
                            String identifier = StringArgumentType.getString(ctx,"dialogue");
                            // set outer properties
                            outer.addScoreboardTag(identifier);
                            outer.setInteractionHeight(3f);
                            outer.setInteractionWidth(3f);
                            // set inner properties
                            inner.addScoreboardTag(identifier);
                            inner.setInteractionHeight(1.825f);
                            inner.setInteractionWidth(0.65f);
                            // broadcast message
                            p.sendMessage(Messages.INTERACTION_ENTITY);
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )
                .then(Commands.literal("test")
                    .executes(ctx -> {
                        createStatsLeaderBoard(Bukkit.getPlayer(ctx.getSource().getSender().getName()).getLocation());
                        return Command.SINGLE_SUCCESS;
                    })
                )
                .build()
            );
        });
    }
}
