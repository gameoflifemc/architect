package cc.architect.tasks.farming;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.BoundingBox;

public class AutoPot {
    public Location start;
    public Location end;

    public Material material;

    public BoundingBox boundingBox;

    public AutoPot(Location start, Location end, Material material) {
        this.start = start;
        this.end = end;
        this.material = material;
        this.boundingBox = BoundingBox.of(start.add(0,1,0), end.add(1,-1,1));
    }
}
