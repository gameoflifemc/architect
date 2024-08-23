package cc.architect.minigames.travel.wrapper;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public abstract class Factory {
    public abstract void create(Location loc);

    public abstract EntityType entityType();
}
