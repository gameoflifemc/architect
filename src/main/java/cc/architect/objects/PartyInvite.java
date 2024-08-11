package cc.architect.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
public class PartyInvite {
    private final String sender;

    @Setter
    private String receiverServer="0";
    @Setter
    private String senderServer="0";

    @Setter
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
    
    public boolean isSameServer(){
        return receiverServer.equals(senderServer);
    }
}
