package cc.architect.objects;

import cc.architect.records.RoughLocation;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HashMaps {
    public static final HashMap<Player,Compass> COMPASSES = new HashMap<>();
    public static final HashMap<Player,List<RoughLocation>> WORLD_LOCATIONS = new HashMap<>();
    public static final HashMap<Player,HashMap<Integer,RoughLocation>> ROTATION_LOCATIONS = new HashMap<>();
    public static final HashMap<Player,ItemStack> REPLACEMENT_ITEM = new HashMap<>();
    public static final Map<Player,Float> BONUSES = new HashMap<>();
    public static final HashMap<String,PartyHolder> PARTIES = new HashMap<>();
    public static final Set<String> IS_IN_PARTY = new HashSet<>();
    public static final Set<Player> ACTION_BAR = new HashSet<>();
}
