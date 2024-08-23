package cc.architect.managers;

import cc.architect.Architect;
import net.luckperms.api.model.data.NodeMap;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Meta {
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
    public static String getSafe(User player, String key, String defau) {
        String val = get(player,key);
        if(val == null) {
            set(player, key, defau);
            val = defau;
        }
        return val;
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
    public static void set(UUID player, String key, String value) {
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
    private static User toUser(Player player) {
        return Architect.LUCKPERMS.getPlayerAdapter(Player.class).getUser(player);
    }
    public static User toUser(UUID player) {
        return Architect.LUCKPERMS.getUserManager().loadUser(player).join();
    }
}
