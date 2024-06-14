package cc.architect.objects;

import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Vector;

public class DialoguePosition {
    private final Location loc;
    private final float yaw;
    private final float pitch;
    private ItemDisplay camera;
    public DialoguePosition(Location origin, Location target) {
        // use origin as base
        loc = origin;
        // camera placeholder
        camera = null;
        // subtract vectors
        Vector vec = origin.toVector().subtract(target.toVector()).normalize();
        // calculate yaw and pitch
        yaw = toFloat(180 - Math.toDegrees(Math.atan2(vec.getX(), vec.getZ())));
        pitch = toFloat(90 - Math.toDegrees(Math.acos(vec.getY())));
        // set new yaw and pitch
        loc.setYaw(yaw);
        loc.setPitch(pitch);
    }
    private float toFloat(double value) {
        // invert value if over 180
        return value > 180 ? (float) -(360 - value) : (float) value;
    }
    public void setCamera(ItemDisplay cameraEntity) {
        camera = cameraEntity;
    }
    public ItemDisplay getCamera() {
        return camera;
    }
    public Location getLocation() {
        return loc;
    }
    public float getYaw() {
        return yaw;
    }
    public float getPitch() {
        return pitch;
    }
}
