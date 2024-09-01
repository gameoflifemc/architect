package cc.architect.minigames.travel.wrapper;

import cc.architect.Architect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public abstract class Factory {
    protected abstract Entity create(Location loc);
    public Entity createSuper(Location loc) {
        Entity e = create(loc);
        e.addScoreboardTag(Architect.SESSION);
        return e;
    }

    public abstract EntityType entityType();
}
