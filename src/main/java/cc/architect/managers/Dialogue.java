package cc.architect.managers;

import cc.architect.objects.DialogueRotations;
import cc.architect.objects.TargetRotations;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static cc.architect.Architect.plugin;
import static cc.architect.Utilities.getRotationToEntity;
import static cc.architect.Utilities.givePlayerDialogOverlay;
import static org.bukkit.Bukkit.getScheduler;

public class Dialogue {
    public static HashMap<Player, DialogueRotations> dialogueRotations = new HashMap<>();
    public static List<Player> playersInDialogue = new ArrayList<>();
    private static final int ROTATION_BASE = 3;
    private static final int ROTATION_SCALING = 3;
    private static final int ROTATION_MARGIN = 1;
    public static void enterDialogue(Player p, Location target) {
        // add player to dialogue
        dialogueRotations.put(p, new DialogueRotations());
        // start repeating task
        getScheduler().runTaskTimer(plugin, (task) -> {
            // cancel task if player is no longer in dialogue
            if (!dialogueRotations.containsKey(p)) {
                task.cancel();
            }
            // get previous target rotation
            DialogueRotations rots = dialogueRotations.getOrDefault(p,null);
            if (rots == null) {
                playersInDialogue.remove(p);
                task.cancel();
                return;
            }
            // get yaw and pitch of rotation to entity
            TargetRotations targetRots = getRotationToEntity(p.getLocation(), target);
            // get new target rotations
            float targetYaw = targetRots.getYaw();
            float targetPitch = targetRots.getPitch();
            // save new target rotations
            rots.setTargetYaw(targetYaw);
            rots.setTargetPitch(targetPitch);
            // get new current yaw and pitch
            float newCurrentYaw = p.getYaw();
            float newCurrentPitch = p.getPitch();
            // check if yaw and pitch are the same
            if (newCurrentYaw != targetYaw || newCurrentPitch != targetPitch) {
                if (newCurrentYaw == rots.getCurrentYaw() || newCurrentPitch == rots.getCurrentPitch()) {
                    alignRotation(p);
                }
            }
            // rewrite current rotations
            rots.setCurrentYaw(newCurrentYaw);
            rots.setCurrentPitch(newCurrentPitch);
            // save result
            dialogueRotations.put(p, rots);
        },0,10);
        // give player overlay
        givePlayerDialogOverlay(p);
    }
    public static void leaveDialogue(Player p) {
        // remove player from dialogue
        dialogueRotations.remove(p);
        playersInDialogue.remove(p);
        // remove overlay
        p.getInventory().setHelmet(ItemStack.empty());
    }
    public static void alignRotation(Player p) {
        playersInDialogue.add(p);
        // run repeating task
        getScheduler().runTaskTimer(plugin, (task) -> {
            // player data
            Location loc = p.getLocation();
            DialogueRotations rots = dialogueRotations.getOrDefault(p,null);
            if (rots == null) {
                playersInDialogue.remove(p);
                task.cancel();
                return;
            }
            float targetYaw = rots.getTargetYaw();
            float targetPitch = rots.getTargetPitch();
            // rotations
            float yaw = loc.getYaw();
            float pitch = loc.getPitch();
            // distances
            float yawDis = Math.abs(yaw - targetYaw);
            float pitchDis = Math.abs(pitch - targetPitch);
            // set yaw
            if (yaw != targetYaw) {
                // apply yaw changes
                loc.setYaw(getRotation(yaw, targetYaw, yawDis, pitchDis, 180));
            }
            // set pitch
            if (pitch != targetPitch) {
                // apply pitch changes
                loc.setPitch(getRotation(pitch, targetPitch, pitchDis, yawDis, 90));
            }
            // apply changes to player
            p.teleport(loc);
            // cancel task if finished
            if (yaw == targetYaw && pitch == targetPitch) {
                playersInDialogue.remove(p);
                task.cancel();
            }
        },0,1);
    }
    private static float getRotation(float rot, float targetRot, float distance, float otherDistance, int scaleIndex) {
        // check if rotation is within slower margin zone
        if (distance <= ROTATION_MARGIN) {
            return targetRot;
        }
        // save original distance
        float originDistance = distance;
        if (originDistance > 180) {
            // invert distance if higher
            distance = 360 - distance;
        }
        // border distance
        int borderDistance = ROTATION_BASE + ROTATION_SCALING;
        // absolute border
        float PosBorder = targetRot + borderDistance;
        float NegBorder = targetRot - borderDistance;
        // offset scaling
        float scaledSpeed = (1 - distance / scaleIndex) * ROTATION_SCALING;
        // base offset
        float rotOffset = rot >= NegBorder && rot <= PosBorder ? ROTATION_MARGIN : ROTATION_BASE + scaledSpeed;
        // align offset to ensure a straight line
        float aligner = distance < otherDistance ? distance / otherDistance : 1;
        // positive or negative offset
        int comparator = Float.compare(targetRot, rot);
        // final offset
        float finalOffset;
        if (originDistance <= 180) {
            finalOffset = rotOffset * aligner * comparator;
        } else {
            finalOffset = rotOffset * aligner * -comparator;
        }
        // return newly offset rotation
        return rot + finalOffset;
    }
}
