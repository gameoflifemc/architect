package cc.architect.minigames.travel.wraper;

import cc.architect.Architect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class TravelMinigame {
    public final Set<UUID> players = new HashSet<>();
    public final World endWorld;
    public final World travelWorld = Bukkit.getWorld("travel");

    public TravelMinigame(World finalWorld) {
        this.endWorld = finalWorld;
        createExitScheduler();
    }
    public abstract Location getStartLocation();
    public abstract Location getEndLocation();
    public abstract Location teleportToLocation();
    public abstract void start(UUID player);
    public abstract double getDistance();

    public void addPlayer(UUID player) {
        players.add(player);
    }

    public void removePlayer(UUID player) {
        players.remove(player);
    }

    public void playerEnter(UUID player) {
        addPlayer(player);
        Bukkit.getPlayer(player).teleport(getStartLocation());
        start(player);
    }

    public void playerSuccessExit(UUID player) {
        removePlayer(player);
        Bukkit.getPlayer(player).teleport(teleportToLocation());
    }

    public void createExitScheduler() {
        Bukkit.getScheduler().runTaskTimer(Architect.PLUGIN,()->{
            getEndLocation().getNearbyEntities(getDistance(),getDistance(),getDistance())
                    .stream().filter(e->e instanceof Player)
                    .map(e->e.getUniqueId())
                    .filter(players::contains)
                    .forEach(this::playerSuccessExit);
        },0,10);
    }
}
