package cc.architect.objects;

import java.util.List;

public class PartyHolder {
    private String leader;
    private List<String> members;

    public PartyHolder(String leader) {
        this.leader = leader;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public void addMember(String member) {
        members.add(member);
    }
}
