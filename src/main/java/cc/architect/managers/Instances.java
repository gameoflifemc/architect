package cc.architect.managers;

import cc.architect.objects.Messages;
import net.kyori.adventure.util.TriState;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Instances {
    /**
     * Initializes a new world from a template and applies custom settings
     * @param player The player to create the world for
     * @param template The name of the template
     */
    public static void initialize(Player player, String template) {
        // create world name
        String name = player.getUniqueId().toString();
        // get files
        File originFile = new File(Bukkit.getPluginsFolder().getPath() + File.separator + ".instances" + File.separator + "template" + template);
        File targetFile = new File(Bukkit.getWorldContainer().getPath() + File.separator + name);
        // copy template to new world
        try {
            FileUtils.copyDirectory(originFile,targetFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // create worldCreator from file
        WorldCreator worldCreator = new WorldCreator(name);
        // add custom settings
        worldCreator.keepSpawnLoaded(TriState.FALSE);
        // register world
        worldCreator.createWorld();
        // confirmation message
        player.sendMessage(Messages.INSTANCE_INITIALIZED);
    }
    /**
     * Assimilates a player into a world, meaning they are teleported to the world's spawn
     * @param player The player to assimilate
     */
    public static void assimilate(Player player) {
        // get world
        World world = Bukkit.getWorld(player.getUniqueId().toString());
        if (world == null) {
            return;
        }
        // teleport player to world spawn
        player.teleport(world.getSpawnLocation());
        // send assimilated message
        player.sendMessage(Messages.INSTANCE_ASSIMILATED);
    }
    /**
     * Decommissions a world, meaning it is unloaded and deleted
     * @param player The player this world belongs to
     */
    public static void decommission(Player player) {
        String name = player.getUniqueId().toString();
        // unload world
        Bukkit.unloadWorld(name,false);
        // delete world
        File targetFile = new File(Bukkit.getWorldContainer().getPath() + File.separator + name);
        try {
            FileUtils.deleteDirectory(targetFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // confirmation message
        player.sendMessage(Messages.INSTANCE_DECOMMISSIONED);
    }
}
