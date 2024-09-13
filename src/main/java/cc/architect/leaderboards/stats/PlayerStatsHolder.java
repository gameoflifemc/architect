package cc.architect.leaderboards.stats;

import cc.architect.managers.Meta;
import lombok.Getter;
import lombok.Setter;
import net.luckperms.api.model.user.User;

import java.util.Random;
import java.util.UUID;

import static cc.architect.managers.Meta.toUser;

@Setter @Getter
public class PlayerStatsHolder {
    public int score_total;
    public int score_daily;
    public int emeralds_total;
    public int emeralds_daily;
    public int investice_total;
    public int investice_daily;
    public int pujcky_total;
    public int pujcky_daily;
    public int days_total;
    public String name;
    public UUID uuid;
    public PlayerStatsHolder() {}
    public PlayerStatsHolder(UUID uuid) {
        User user = toUser(uuid);
        this.uuid = uuid;
        this.score_total = Integer.parseInt(Meta.getSafe(user,"score_highest","0"));
        this.score_daily = Integer.parseInt(Meta.getSafe(user,"score_daily","0"));

        this.emeralds_total = Integer.parseInt(Meta.getSafe(user,"emeralds_highest","0"));
        this.emeralds_daily = Integer.parseInt(Meta.getSafe(user,"emeralds_daily","0"));

        this.investice_total = Integer.parseInt(Meta.getSafe(user,"investments_highest","0"));
        this.investice_daily = Integer.parseInt(Meta.getSafe(user,"investments_daily","0"));

        this.pujcky_total = Integer.parseInt(Meta.getSafe(user,"loan_highest","0"));
        this.pujcky_daily = Integer.parseInt(Meta.getSafe(user,"loan_daily","0"));

        this.days_total = Integer.parseInt(Meta.getSafe(user,"days","0"));
    }
    public PlayerStatsHolder(int score_total, int emeralds_total, int days_total, int score_daily, int emeralds_daily, String name, UUID uuid) {
        this.score_total = score_total;
        this.score_daily = score_daily;
        this.emeralds_total = emeralds_total;
        this.emeralds_daily = emeralds_daily;
        this.days_total = days_total;
        this.name = name;
        this.uuid = uuid;
    }

    public PlayerStatsHolder(int max){
        Random rand = new Random();
        this.uuid = UUID.randomUUID();
        this.score_total = rand.nextInt(max);
        this.emeralds_total = rand.nextInt(max);
        this.days_total = rand.nextInt(max);
        this.score_daily = rand.nextInt(max);
        this.emeralds_daily = rand.nextInt(max);
        this.investice_total = rand.nextInt(max);
        this.investice_daily = rand.nextInt(max);
        this.pujcky_total = rand.nextInt(max);
        this.pujcky_daily = rand.nextInt(max);
    }
}
