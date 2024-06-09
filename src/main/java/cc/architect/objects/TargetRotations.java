package cc.architect.objects;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static cc.architect.Utilities.roughlyEquals;

public class TargetRotations {
    private float yaw;
    private float pitch;
    private float toFloat(double value) {
        // invert value if over 180
        return value > 180 ? (float) -(360 - value) : (float) value;
    }
    public TargetRotations() {
        this.yaw = 0;
        this.pitch = 0;
    }
    public void calculate(Location origin, Location target) {
        // get vectors
        Vector originVec = origin.toVector();
        Vector targetVec = target.toVector();
        // subtract vectors
        Vector dirVec = originVec.subtract(targetVec).normalize();
        // calculate yaw and pitch
        double yaw = 180 - Math.toDegrees(Math.atan2(dirVec.getX(), dirVec.getZ()));
        double pitch = 90 - Math.toDegrees(Math.acos(dirVec.getY()));
        // set new yaw and pitch
        this.yaw = toFloat(yaw);
        this.pitch = toFloat(pitch);
    }
    public boolean compare(float yaw, float pitch) {
        return !roughlyEquals(yaw, this.yaw) || !roughlyEquals(pitch, this.pitch);
    }
    public float getYaw() {
        return yaw;
    }
    public float getPitch() {
        return pitch;
    }
}
