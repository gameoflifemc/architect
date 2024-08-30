package cc.architect.minigames.travel.wrapper;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public abstract class Factory {
    public abstract Entity create(Location loc);

    public abstract EntityType entityType();
}
