package cc.architect.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartyHolder {
    private String leader;
    private List<String> members = new ArrayList<>();

    public PartyHolder(String leader) {
        this.leader = leader;
    }
    public PartyHolder(String leader, String member) {
        this.leader = leader;
        members.add(member);
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

    public void removeMember(String member) {
        members.remove(member);
    }
    @Override
    public String toString() {
        return "PartyHolder{leader='" + leader + "', members=" + String.join(", ",members) + "}";
    }

    public static PartyHolder getMemberParty(String member) {
        for (PartyHolder party : HashMaps.PARTIES.values()) {
            if (party.getMembers().contains(member)) {
                return party;
            }
        }
        return null;
    }
}
