package cc.architect.minigames.travel.wraper;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class TravelMinigame {
    public final World endWorld;
    public final World travelWorld = Bukkit.getWorld("travel");

    public TravelMinigame(World finalWorld) {
        this.endWorld = finalWorld;
    }
    public abstract Location getStartLocation();
    public abstract Location getEndLocation();
    public abstract Location teleportToLocation();
    public abstract void start(UUID player);
    public abstract double getDistance();

    public void playerEnter(UUID player) {
        Bukkit.getPlayer(player).teleport(getStartLocation());
        start(player);
    }

    public void playerSuccessExit(UUID player) {
        Bukkit.getPlayer(player).teleport(teleportToLocation());
    }

    public void handeExit() {
        getEndLocation().getNearbyEntities(getDistance(),getDistance(),getDistance())
                .stream().filter(e->e instanceof Player)
                .map(e->e.getUniqueId())
                .forEach(this::playerSuccessExit);
    }
}
