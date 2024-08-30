package cc.architect.minigames.travel.wrapper;

import cc.architect.Architect;
import cc.architect.managers.Movers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class TravelMinigame {
    public final World endWorld;
    public final World travelWorld = Bukkit.getWorld("travel");
    public int updateRate;

    public TravelMinigame(World finalWorld, int updateRate) {
        this.endWorld = finalWorld;
        this.updateRate = updateRate;
        createTask();
    }
    public abstract Location getStartLocation();
    public abstract Location getEndLocation();
    public abstract Location teleportToLocation();
    public abstract void start(UUID player);
    public abstract double getDistance();
    public abstract void updateCall();
    public abstract boolean canExit(UUID player);

    public void playerEnter(UUID player) {
        Movers.toTravel(Bukkit.getPlayer(player),getStartLocation());
        start(player);
    }

    public void playerSuccessExit(UUID player) {
        Bukkit.getPlayer(player).teleport(teleportToLocation());
    }

    public void handeExit() {
        getEndLocation().getNearbyEntities(getDistance(),getDistance(),getDistance())
                .stream().filter(e->e instanceof Player)
                .map(e->e.getUniqueId())
                .filter(this::canExit)
                .forEach(this::playerSuccessExit);
    }

    public void createTask(){
        Architect.SCHEDULER.runTaskTimer(Architect.PLUGIN,()->{
            updateCall();
        },updateRate,updateRate);
    }

}
