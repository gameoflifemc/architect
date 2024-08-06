package cc.architect.leaderboards.stats;

import lombok.Getter;
import lombok.Setter;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.jetbrains.annotations.Contract;

import java.beans.ConstructorProperties;
import java.util.UUID;

@Setter @Getter
public class PlayerStatsHolder {
    public int money;
    public int score;
    public int debt;
    public int netWorth;

    public String name;
    public UUID uuid;
    public PlayerStatsHolder() {}
    public PlayerStatsHolder(UUID uuid) {
        this.uuid = uuid;
        User user = LuckPermsProvider.get().getUserManager().getUser(uuid);
        if(user == null) {
            return;
        }
        name = user.getUsername();

        this.money = Integer.parseInt(getValue(user,"money"));
        this.score = Integer.parseInt(getValue(user,"score"));
        this.debt = Integer.parseInt(getValue(user,"debt"));
        this.netWorth = Integer.parseInt(getValue(user,"netWorth"));
    }
    public PlayerStatsHolder(int money, int score, int debt, int netWorth, UUID uuid, String name) {
        this.money = money;
        this.score = score;
        this.debt = debt;
        this.netWorth = netWorth;

        this.uuid = uuid;
        this.name = name;
    }
    public void setMeta(User user, String key, String value) {
        MetaNode node = MetaNode.builder(key, value).build();
        user.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals(key)));
        user.data().add(node);
        LuckPermsProvider.get().getUserManager().saveUser(user);
    }
    public String getValue(User user, String key) {
        String val;
        String data = user.getCachedData().getMetaData().getMetaValue(key);
        if(data == null) {
            setMeta(user, key, "0");
            val = "0";
        } else {
            val = data;
        }
        return val;
    }
}
