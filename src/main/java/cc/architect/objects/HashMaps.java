package cc.architect.objects;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HashMaps {
    public static final HashMap<String, PartyHolder> PARTIES = new HashMap<>();
    public static final Set<String> IS_IN_PARTY = new HashSet<>();
    public static final HashMap<Player, Integer> ROUTINES = new HashMap<>();
    public static final Map<Player, Float> BONUSES = new HashMap<>();
    public static final HashMap<Player, BossBar> COMPASSES = new HashMap<>();
}
