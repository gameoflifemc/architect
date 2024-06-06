package cc.architect.objects;

public class TargetRotations {
    private float yaw;
    private float pitch;
    public TargetRotations() {
        this.yaw = 0;
        this.pitch = 0;
    }
    public float getYaw() {
        return yaw;
    }
    public float getPitch() {
        return pitch;
    }
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
