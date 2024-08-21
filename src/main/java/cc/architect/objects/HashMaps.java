package cc.architect.objects;

import org.bukkit.entity.Player;

import java.util.*;

public class HashMaps {
    public static final HashMap<String,PartyHolder> PARTIES = new HashMap<>();
    public static final Set<String> IS_IN_PARTY = new HashSet<>();
    public static final HashMap<Player,Integer> ROUTINES = new HashMap<>();
    public static final Map<Player,Float> BONUSES = new HashMap<>();
    public static final HashMap<Player,Compass> COMPASSES = new HashMap<>();
    public static final HashMap<Player,List<RoughLocation>> WORLD_LOCATIONS = new HashMap<>();
    public static final HashMap<Player,HashMap<Integer,RoughLocation>> ROTATION_LOCATIONS = new HashMap<>();
}
