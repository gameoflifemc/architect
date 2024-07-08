package cc.architect.objects;

public class PartyInvite {
    private String sender;

    private String receiverServer="0";
    private String senderServer="0";

    private int taskID;

    public PartyInvite(String sender) {
        this.sender = sender;
    }
    public PartyInvite(String sender, int taskID) {
        this.sender = sender;
        this.taskID = taskID;
    }
    public PartyInvite(String sender, int taskID, String receiverServer, String senderServer) {
        this.sender = sender;
        this.taskID = taskID;
        this.receiverServer = receiverServer;
        this.senderServer = senderServer;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiverServer() {
        return receiverServer;
    }

    public void setReceiverServer(String receiverServer) {
        this.receiverServer = receiverServer;
    }

    public String getSenderServer() {
        return senderServer;
    }

    public void setSenderServer(String senderServer) {
        this.senderServer = senderServer;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public boolean isSameServer(){
        return receiverServer.equals(senderServer);
    }
}
