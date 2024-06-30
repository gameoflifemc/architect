package cc.architect.managers;

import net.kyori.adventure.util.TriState;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.util.FileUtil;

import java.io.File;

public class Instances {
    public static void initialize(String name, String template) {
        // copy template to make a new world
        FileUtil.copy(new File("plugins/.instances/" + template),new File(name));
        // create worldCreator from file
        WorldCreator worldCreator = new WorldCreator(name);
        // add custom settings
        worldCreator.keepSpawnLoaded(TriState.FALSE);
        // register world
        worldCreator.createWorld();
    }
    public static void assimilate(Player p) {
        // get world
        World world = Bukkit.getWorld(p.getName());
        if (world == null) {
            return;
        }
        // teleport player to world spawn
        p.teleport(world.getSpawnLocation());
    }
    public static void decommission(String name) {
        // unload world
        Bukkit.unloadWorld(name,false);
    }
}
