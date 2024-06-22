package cc.architect.managers;

import net.kyori.adventure.util.TriState;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Instances {
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
