package cc.architect.leaderboards.stats;

import cc.architect.managers.Meta;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Setter @Getter
public class PlayerStatsHolder {
    public int score_total;
    public int emeralds_total;
    public int days_total;
    public int score_daily;
    public int emeralds_daily;
    public String name;
    public UUID uuid;
    public PlayerStatsHolder() {}
    public PlayerStatsHolder(UUID uuid) {
        this.uuid = uuid;
        this.score_total = Integer.parseInt(Meta.getSafe(uuid,"score_total","0"));
        this.emeralds_total = Integer.parseInt(Meta.getSafe(uuid,"emeralds_total","0"));
        this.days_total = Integer.parseInt(Meta.getSafe(uuid,"days_total","0"));
        this.score_daily = Integer.parseInt(Meta.getSafe(uuid,"score_daily","0"));
        this.emeralds_daily = Integer.parseInt(Meta.getSafe(uuid,"emeralds_daily","0"));
    }
    public PlayerStatsHolder(int score_total, int emeralds_total, int days_total, int score_daily, int emeralds_daily, String name, UUID uuid) {
        this.score_total = score_total;
        this.emeralds_total = emeralds_total;
        this.days_total = days_total;
        this.score_daily = score_daily;
        this.emeralds_daily = emeralds_daily;
        this.name = name;
        this.uuid = uuid;
    }
}
