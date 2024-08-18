package cc.architect.minigames.travel;

import cc.architect.minigames.travel.wraper.BasicTravelMinigame;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class MineTravel extends BasicTravelMinigame {
    public MineTravel() {
        super(Bukkit.getWorld("mine"));
    }

    @Override
    public void start(UUID player) {

    }

    @Override
    public double getDistance() {
        return 5;
    }

    @Override
    public Location getStartLocation() {
        return new Location(travelWorld, -31, 126, -47);
    }

    @Override
    public Location getEndLocation() {
        return new Location(travelWorld, -10, 126, 43);
    }

    @Override
    public Location teleportToLocation() {
        return new Location(endWorld, 32, -60, -106,-54,0);
    }
}
