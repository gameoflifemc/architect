package cc.architect;

import net.kyori.adventure.util.TriState;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cc.architect.Architect.plugin;
import static org.bukkit.Bukkit.getScheduler;

public class Utilities {
    private static final int ROTATION_BASE = 3;
    private static final int ROTATION_SCALING = 3;
    private static final int ROTATION_MARGIN = 2;
    public static void alignRotation(Player p, int targetYaw, int targetPitch) {
        // run repeating task
        getScheduler().runTaskTimer(plugin, (alignmentTask) -> {
            // location
            Location loc = p.getLocation();
            // rotations
            float yaw = loc.getYaw();
            float pitch = loc.getPitch();
            // absolute values
            float absYaw = Math.abs(yaw);
            float absPitch = Math.abs(pitch);
            // set yaw
            if (yaw != targetYaw) {
                // apply yaw changes
                loc.setYaw(getRotation(yaw, absYaw, targetYaw, absPitch, 180));
            }
            // set pitch
            if (pitch != targetPitch) {
                // apply pitch changes
                loc.setPitch(getRotation(pitch, absPitch, targetPitch, absYaw, 90));
            }
            // apply changes to player
            p.teleport(loc);
            // cancel task if finished
            if (yaw == targetYaw && pitch == targetPitch) {
                alignmentTask.cancel();
            }
        },0,1);
    }
    private static float getRotation(float rot, float absRot, float targetRot, float absOtherRot, int scaleIndex) {
        // check if rotation is within margin
        if (absRot <= ROTATION_MARGIN) {
            return targetRot;
        }
        // get border
        float rotBorder = targetRot + ROTATION_BASE + ROTATION_SCALING;
        // scaling
        float scaledSpeed = (1 - (absRot - targetRot) / scaleIndex) * ROTATION_SCALING;
        // base offset
        float rotOffset = absRot <= rotBorder ? ROTATION_MARGIN : ROTATION_BASE + scaledSpeed;
        // scale offset to ensure a straight line
        rotOffset *= absOtherRot > absRot ? absRot / absOtherRot : 1;
        // positive or negative offset
        rotOffset = rot > targetRot ? -rotOffset : rot < targetRot ? rotOffset : 0;
        // return newly offset rotation
        return rot + rotOffset;
    }
    public static World initializeSimulation(String worldName) {
        WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.environment(World.Environment.NORMAL);
        worldCreator.keepSpawnLoaded(TriState.FALSE);
        worldCreator.generateStructures(true);
        worldCreator.type(WorldType.NORMAL);
        worldCreator.biomeProvider(new BiomeProvider() {
            @Override
            public @NotNull Biome getBiome(@NotNull WorldInfo worldInfo, int i, int i1, int i2) {
                return Biome.PLAINS;
            }
            @Override
            public @NotNull List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
                return List.of(Biome.PLAINS);
            }
        });
        return worldCreator.createWorld();
    }
}
