package cc.architect.leaderboards.stats;

import cc.architect.managers.Meta;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
        Player p = Bukkit.getPlayer(uuid);
        if (p == null) {
            return;
        }
        name = p.getName();
        this.money = Integer.parseInt(Meta.get(p,"money"));
        this.score = Integer.parseInt(Meta.get(p,"score"));
        this.debt = Integer.parseInt(Meta.get(p,"debt"));
        this.netWorth = Integer.parseInt(Meta.get(p,"netWorth"));
    }
    public PlayerStatsHolder(int money, int score, int debt, int netWorth, UUID uuid, String name) {
        this.money = money;
        this.score = score;
        this.debt = debt;
        this.netWorth = netWorth;
        this.uuid = uuid;
        this.name = name;
    }
}
