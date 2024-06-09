package cc.architect.objects;

import org.bukkit.Location;

public class DialoguePosition {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    public DialoguePosition(Location loc) {
        rewrite(loc);
    }
    public void rewrite(Location loc) {
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
        this.yaw = loc.getYaw();
        this.pitch = loc.getPitch();
    }
    public boolean equals(Location loc) {
        return loc.getX() == x && loc.getY() == y && loc.getZ() == z && loc.getYaw() == yaw && loc.getPitch() == pitch;
    }
}
