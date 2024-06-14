package cc.architect.managers;

import cc.architect.objects.DialoguePosition;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

import static cc.architect.Architect.plugin;
import static cc.architect.Utilities.givePlayerDialogOverlay;
import static org.bukkit.Bukkit.getScheduler;

public class Dialogue {
    public static final HashMap<Player, DialoguePosition> dialoguePositions = new HashMap<>();
    public static void enterDialogue(Player p, Location targetLoc) {
        // add player to dialogue
        dialoguePositions.put(p, new DialoguePosition(p.getLocation(), targetLoc));
        // force player to look at target
        forcePlayerRotation(p);
        // give player overlay
        givePlayerDialogOverlay(p);
    }
    public static void forcePlayerRotation(Player p) {
        // entity location
        Location cameraLoc = p.getEyeLocation();
        // summon camera entity
        ItemDisplay camera = cameraLoc.getWorld().spawn(cameraLoc, ItemDisplay.class);
        // set camera's teleport duration
        camera.setTeleportDuration(20);
        // put player in spectator mode
        p.setGameMode(GameMode.SPECTATOR);
        // set camera entity as the player's camera
        p.setSpectatorTarget(camera);
        // get target location
        DialoguePosition pos = dialoguePositions.get(p);
        // save camera entity to dialogue position
        pos.setCamera(camera);
        // new camera location
        Location newCameraLoc = cameraLoc.clone();
        // set target rotations
        newCameraLoc.setYaw(pos.getYaw());
        newCameraLoc.setPitch(pos.getPitch());
        // teleport camera entity to target location
        getScheduler().runTaskLater(plugin, () -> {
            // teleport camera entity
            camera.teleport(newCameraLoc);
        },2);
    }
    public static void leaveDialogue(Player p) {
        // get player's dialogue position
        DialoguePosition pos = dialoguePositions.get(p);
        // remove player from camera
        p.setSpectatorTarget(null);
        // teleport player back to original location
        p.teleport(pos.getLocation());
        // put player back in adventure mode
        p.setGameMode(GameMode.ADVENTURE);
        // remove camera entity
        pos.getCamera().remove();
        // remove player from dialogue
        dialoguePositions.remove(p);
        // remove overlay
        p.getInventory().setHelmet(ItemStack.empty());
    }
}
