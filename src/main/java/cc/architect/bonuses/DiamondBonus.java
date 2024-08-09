package cc.architect.bonuses;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class DiamondBonus {
    public static Map<Player, Float> bonuses = new HashMap<>();

    public static void initPlayer(Player player) {
        if (!bonuses.containsKey(player)) {
            bonuses.put(player, 1f);
        }
    }

    public static void add(Player player, float amount) {
        float current = bonuses.get(player);
        bonuses.remove(player);
        bonuses.put(player, amount+current);
    }
}
