package cc.architect.objects;

public class DialogueRotations {
    private float currentYaw;
    private float currentPitch;
    private float targetYaw;
    private float targetPitch;
    public DialogueRotations() {
        this.currentYaw = 0;
        this.currentPitch = 0;
        this.targetYaw = 0;
        this.targetPitch = 0;
    }
    public float getCurrentYaw() {
        return currentYaw;
    }
    public float getCurrentPitch() {
        return currentPitch;
    }
    public float getTargetYaw() {
        return targetYaw;
    }
    public float getTargetPitch() {
        return targetPitch;
    }
    public void setCurrentYaw(float yaw) {
        this.currentYaw = yaw;
    }
    public void setCurrentPitch(float pitch) {
        this.currentPitch = pitch;
    }
    public void setTargetYaw(float yaw) {
        this.targetYaw = yaw;
    }
    public void setTargetPitch(float pitch) {
        this.targetPitch = pitch;
    }
}
