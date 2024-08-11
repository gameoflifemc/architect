package cc.architect.objects;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
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
