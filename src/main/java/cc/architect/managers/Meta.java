package cc.architect.managers;

import cc.architect.Architect;
import net.luckperms.api.model.data.NodeMap;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.entity.Player;

public class Meta {
    public static void addValue(Player player, String key, int value) {
        addValue(toUser(player), key, value);
    }
    public static void addValue(User user, String key, int value) {
        int current = user.getCachedData().getMetaData().getMetaValue(key,Integer::parseInt).orElse(0);
        setValue(user, key, Integer.toString(current + value));
    }
    public static String getValue(Player player, String key) {
        return getValue(toUser(player), key);
    }
    public static String getValue(User user, String key) {
        return user.getCachedData().getMetaData().getMetaValue(key);
    }
    public static void setValue(Player player, String key, String value) {
        setValue(toUser(player), key, value);
    }
    public static void setValue(User user, String key, String value) {
        NodeMap map = user.data();
        map.clear(NodeType.META.predicate(n -> n.getMetaKey().equals(key)));
        map.add(MetaNode.builder(key, value).build());
        Architect.LUCKPERMS.getUserManager().saveUser(user);
    }
    public static User toUser(Player player) {
        return Architect.LUCKPERMS.getPlayerAdapter(Player.class).getUser(player);
    }
}
