package cc.architect.leaderboards.stats;

import lombok.Getter;
import lombok.Setter;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import cc.architect.Utilities;

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
    public String getValue(User user, String key) {
        String val;
        String data = user.getCachedData().getMetaData().getMetaValue(key);
        if(data == null) {
            Utilities.setMeta(user, key, "0");
            val = "0";
        } else {
            val = data;
        }
        return val;
    }
}
