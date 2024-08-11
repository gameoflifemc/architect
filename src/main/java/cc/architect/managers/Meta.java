package cc.architect.managers;

import cc.architect.Architect;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.entity.Player;

public class Meta {
    public static void addValue(Player player, String key, int value) {
        addValue(toUser(player), key, value);
    }
    public static void addValue(User user, String key, int value) {
        int val = user.getCachedData().getMetaData().getMetaValue(key, Integer::parseInt).orElse(0);
        setValue(user, key, Integer.toString(val + value));
    }
    public static String getValue(Player player, String key) {
        return getValue(toUser(player), key);
    }
    public static String getValue(User user, String key) {
        return user.getCachedData().getMetaData().getMetaValue(key);
    }
    public static void setValue(User user, String key, String value) {
        user.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals(key)));
        user.data().add(MetaNode.builder(key, value).build());
        Architect.LUCKPERMS.getUserManager().saveUser(user);
    }
    public static User toUser(Player player) {
        return Architect.LUCKPERMS.getPlayerAdapter(Player.class).getUser(player);
    }
}
