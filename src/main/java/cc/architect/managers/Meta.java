package cc.architect.managers;

import cc.architect.Architect;
import net.luckperms.api.model.data.NodeMap;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Meta {
    // autosaved data
    public static final String LAST_LOCATION = "last_location";
    // game flow data
    public static final String DAYS = "days";
    public static final String IS_IN_GAME = "is_in_game";
    public static final String ROUTINE = "routine";
    // player data
    public static final String ACTIONS = "actions";
    public static final String FACT = "fact_";
    public static final String SAVINGS = "savings";
    public static final String CODE = "code";
    // total values
    public static final String SCORE_TOTAL = "score_total";
    public static final String EMERALDS_TOTAL = "emeralds_total";
    public static final String INVESTMENTS_TOTAL = "investments_total";
    public static final String INVESTMENTS_MAP = "investments_map";
    public static final String INVESTMENTS_MAP_RISKY = "investments_map_risky";
    public static final String LOAN_TOTAL = "loan_total";
    public static final String LOAN_RISKY_MAP = "loan_risky_map";
    public static final String LOAN_RISKY_COUNTER = "loan_risky_counter";
    public static final String LOAN_SAFE = "loan_spor";
    public static final String LOAN_SAFE_HAD_LOAN = "loan_safe_had_loan";
    // highest values
    public static final String SCORE_HIGHEST = "score_highest";
    public static final String EMERALDS_HIGHEST = "emeralds_highest";
    public static final String INVESTMENTS_HIGHEST = "investments_highest";
    public static final String LOAN_HIGHEST = "loan_highest";
    // daily values
    public static final String SCORE_DAILY = "score_daily";
    public static final String EMERALDS_DAILY = "emeralds_daily";
    public static final String INVESTMENTS_DAILY = "investments_daily";
    public static final String LOAN_DAILY = "loan_daily";
    public static boolean check(Player player, String key) {
        return check(toUser(player), key);
    }
    private static boolean check(User user, String key) {
        return user.getCachedData().getMetaData().getMeta().containsKey(key);
    }
    public static void clear(Player player, String key) {
        clear(toUser(player), key);
    }
    private static void clear(User user, String key) {
        NodeMap map = user.data();
        map.clear(NodeType.META.predicate(n -> n.getMetaKey().equals(key)));
        Architect.LUCKPERMS.getUserManager().saveUser(user);
    }
    public static String getSafe(User player, String key, String defaultValue) {
        String value = get(player,key);
        if (value == null) {
            set(player,key,defaultValue);
            return defaultValue;
        }
        return value;
    }
    public static String get(Player player, String key) {
        return get(toUser(player), key);
    }
    public static String get(UUID player, String key) {
        return get(toUser(player), key);
    }
    private static String get(User user, String key) {
        return user.getCachedData().getMetaData().getMetaValue(key);
    }
    public static void set(Player player, String key, String value) {
        set(toUser(player), key, value);
    }
    private static void set(User user, String key, String value) {
        NodeMap map = user.data();
        map.clear(NodeType.META.predicate(n -> n.getMetaKey().equals(key)));
        map.add(MetaNode.builder(key, value).build());
        Architect.LUCKPERMS.getUserManager().saveUser(user);
    }
    public static void add(Player player, String key, int value) {
        add(toUser(player), key, value);
    }
    private static void add(User user, String key, int value) {
        int current = user.getCachedData().getMetaData().getMetaValue(key,Integer::parseInt).orElse(0);
        int next = current + value;
        if (next < 0) {
            next = 0;
        }
        set(user, key, Integer.toString(next));
    }
    public static User toUser(Player player) {
        return Architect.LUCKPERMS.getPlayerAdapter(Player.class).getUser(player);
    }
    public static User toUser(UUID player) {
        return Architect.LUCKPERMS.getUserManager().loadUser(player).join();
    }
}
