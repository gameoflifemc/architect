package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.Utilities;
import cc.architect.objects.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Dialogues {
    public static void enter(Player p, Location targetLoc, String id) {
        // add player to dialogue
        HashMaps.DIALOGUE_POSITIONS.put(p, new DialoguePosition(p.getLocation(), targetLoc));
        // force player to look at target
        Dialogues.createCamera(p);
        // give player overlay
        Utilities.giveOverlay(p);
        // send controls
        p.sendActionBar(Messages.STANDARD);
        
        
        
        // prepare response list
        ResponseList responseList = new ResponseList(id);
        // add response list to response lists
        HashMaps.RESPONSE_LISTS.put(p, responseList);
        // create a new objective
        Scoreboards.create(p);
        // show player responses
        Scoreboards.show(p);
        // send controls
        p.sendActionBar(Messages.RESPONSE);
    }
    public static void leave(Player p) {
        // get player's dialogue position
        DialoguePosition pos = HashMaps.DIALOGUE_POSITIONS.get(p);
        // remove player from camera
        p.setSpectatorTarget(p);
        // teleport player back to original location
        p.teleport(pos.getLocation());
        // put player back in adventure mode
        p.setGameMode(GameMode.ADVENTURE);
        // remove camera entity
        pos.getCamera().remove();
        // remove objective
        Scoreboards.remove(p);
        // remove player from dialogue
        HashMaps.DIALOGUE_POSITIONS.remove(p);
        // remove overlay
        p.getInventory().setHelmet(ItemStack.empty());
        // send empty action bar
        p.sendActionBar(Component.empty());
    }
    private static void createCamera(Player p) {
        // entity location
        Location cameraLoc = p.getEyeLocation();
        // summon camera entity
        ItemDisplay camera = cameraLoc.getWorld().spawn(cameraLoc,ItemDisplay.class);
        // set camera's teleport duration
        camera.setTeleportDuration(20);
        // put player in spectator mode
        p.setGameMode(GameMode.SPECTATOR);
        // set camera entity as the player's camera
        p.setSpectatorTarget(camera);
        // get target location
        DialoguePosition pos = HashMaps.DIALOGUE_POSITIONS.get(p);
        // save camera entity to dialogue position
        pos.setCamera(camera);
        // new camera location
        Location newCameraLoc = cameraLoc.clone();
        // set target rotations
        newCameraLoc.setYaw(pos.getYaw());
        newCameraLoc.setPitch(pos.getPitch());
        // teleport camera entity to target location
        Bukkit.getScheduler().runTaskLater(Architect.PLUGIN, () -> {
            // teleport camera entity
            camera.teleport(newCameraLoc);
        },2);
    }
}
