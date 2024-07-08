package cc.architect.objects;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class HashMaps {
    public static final HashMap<Player, DialoguePosition> DIALOGUE_POSITIONS = new HashMap<>();
    public static final HashMap<Player, ResponseList> RESPONSE_LISTS = new HashMap<>();
    public static final HashMap<String, PartyHolder> PARTIES = new HashMap<>();
    public static final Set<String> CANNOT_MAKE_PARTY = new HashSet<>();
}
