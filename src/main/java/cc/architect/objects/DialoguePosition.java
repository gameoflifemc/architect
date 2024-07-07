package cc.architect.objects;

import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Vector;

public class DialoguePosition {
    private final Location loc;
    private final float yaw;
    private final float pitch;
    private ItemDisplay camera;
    private final String uid;
    /**
     * Holds data about the dialogue position
     * @param origin The origin location
     * @param target The target location
     * @param uid The unique identifier of the dialogue
     */
    public DialoguePosition(Location origin,Location target, String uid) {
        // set uid
        this.uid = uid;
        // use origin as base
        loc = origin;
        // add 0.1 to y to prevent clipping
        loc.add(0,0.1,0);
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
    /**
     * Converts degrees in a double format to a float format
     * @param value The double to convert
     * @return The converted float
     */
    private float toFloat(double value) {
        // invert value if over 180
        return value > 180 ? (float) -(360 - value) : (float) value;
    }
    /**
     * Sets the camera entity
     * @param cameraEntity The camera entity
     */
    public void setCamera(ItemDisplay cameraEntity) {
        camera = cameraEntity;
    }
    /**
     * Gets the camera entity
     * @return The camera entity
     */
    public ItemDisplay getCamera() {
        return camera;
    }
    /**
     * Gets the location
     * @return The location
     */
    public Location getLocation() {
        return loc;
    }
    /**
     * Gets the yaw
     * @return The yaw
     */
    public float getYaw() {
        return yaw;
    }
    /**
     * Gets the pitch
     * @return The pitch
     */
    public float getPitch() {
        return pitch;
    }
    /**
     * Gets the unique identifier
     * @return The unique identifier
     */
    public String getUid() {
        return uid;
    }
}
