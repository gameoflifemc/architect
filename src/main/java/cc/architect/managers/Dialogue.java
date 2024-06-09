package cc.architect.managers;

import cc.architect.objects.DialoguePosition;
import cc.architect.objects.TargetRotations;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static cc.architect.Architect.plugin;
import static cc.architect.Utilities.givePlayerDialogOverlay;
import static cc.architect.Utilities.roughlyEquals;
import static org.bukkit.Bukkit.getScheduler;

public class Dialogue {
    public static final List<Player> playersInDialogue = new ArrayList<>();
    public static final List<Player> playersBeingAligned = new ArrayList<>();
    private static final int ROTATION_BASE = 3;
    private static final int ROTATION_SCALING = 3;
    private static final int ROTATION_SLOWED = 1;
    public static void enterDialogue(Player p, Location target) {
        // add player to dialogue
        playersInDialogue.add(p);
        // prepare current position object
        DialoguePosition currentPos = new DialoguePosition(p.getLocation());
        // prepare target rotations object
        TargetRotations targetRots = new TargetRotations();
        // start repeating task
        getScheduler().runTaskTimer(plugin, (task) -> {
            // cancel task if player is no longer in dialogue
            if (!playersInDialogue.contains(p)) {
                leaveDialogue(p);
                task.cancel();
                return;
            }
            // get current location
            Location playerLoc = p.getLocation();
            // cancel task if player is too far away
            if (playerLoc.distanceSquared(target) > 25) {
                leaveDialogue(p);
                task.cancel();
                return;
            }
            // get target rotations
            targetRots.calculate(playerLoc, target);
            // check if alignment is needed
            if (targetRots.compare(playerLoc.getYaw(), playerLoc.getPitch())) {
                // check if player hasn't moved and isn't already being aligned
                if (currentPos.equals(playerLoc) && !playersBeingAligned.contains(p)) {
                    // align player rotations
                    alignRotations(p, targetRots);
                }
            }
            // rewrite current rotations
            currentPos.rewrite(playerLoc);
        },0,5);
        // give player overlay
        givePlayerDialogOverlay(p);
    }
    public static void alignRotations(Player p, TargetRotations targetRotations) {
        // mark player as being aligned
        playersBeingAligned.add(p);
        // run repeating task
        getScheduler().runTaskTimer(plugin, (task) -> {
            // player data
            Location alignment = p.getLocation().clone();
            // rotations
            float yaw = alignment.getYaw();
            float pitch = alignment.getPitch();
            // target rotations
            float targetYaw = targetRotations.getYaw();
            float targetPitch = targetRotations.getPitch();
            // distances
            float yawDis = Math.abs(yaw - targetYaw);
            float pitchDis = Math.abs(pitch - targetPitch);
            // set yaw
            if (yaw != targetYaw) {
                // apply yaw changes
                alignment.setYaw(getOffsetRotation(yaw, targetYaw, yawDis, pitchDis, 180));
            }
            // set pitch
            if (pitch != targetPitch) {
                // apply pitch changes
                alignment.setPitch(getOffsetRotation(pitch, targetPitch, pitchDis, yawDis, 90));
            }
            // apply changes to player
            p.teleport(alignment);
            // cancel task if finished
            if (roughlyEquals(yaw, targetYaw) && roughlyEquals(pitch, targetPitch)) {
                playersBeingAligned.remove(p);
                task.cancel();
            }
        },0,1);
    }
    private static float getOffsetRotation(float rotation, float targetRotation, float distance, float otherDistance, int scaleIndex) {
        // check if rotation is within slower margin zone
        if (distance <= ROTATION_SLOWED) {
            return targetRotation;
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
        float PositiveBorder = targetRotation + borderDistance;
        float NegativeBorder = targetRotation - borderDistance;
        // offset scaling
        float scaledSpeed = (1 - distance / scaleIndex) * ROTATION_SCALING;
        // base offset
        float rotationOffset = rotation >= NegativeBorder && rotation <= PositiveBorder ? ROTATION_SLOWED : ROTATION_BASE + scaledSpeed;
        // align offset to ensure a straight line
        float aligner = otherDistance > ROTATION_SLOWED && distance < otherDistance ? distance / otherDistance : 1;
        // positive or negative offset
        int comparator = Float.compare(targetRotation, rotation);
        // final offset
        float finalOffset = rotationOffset * aligner;
        // multiply offset by comparator
        if (originDistance <= 180) {
            finalOffset *= comparator;
        } else {
            finalOffset *= -comparator;
        }
        // return newly offset rotation
        return rotation + finalOffset;
    }
    public static void leaveDialogue(Player p) {
        // remove player from dialogue
        playersInDialogue.remove(p);
        playersBeingAligned.remove(p);
        // remove overlay
        p.getInventory().setHelmet(ItemStack.empty());
    }
}
