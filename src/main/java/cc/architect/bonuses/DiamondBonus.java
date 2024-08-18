package cc.architect.bonuses;

import cc.architect.objects.HashMaps;
import org.bukkit.entity.Player;

public class DiamondBonus {
    public static void initPlayer(Player player) {
        if (!HashMaps.BONUSES.containsKey(player)) {
            HashMaps.BONUSES.put(player, 1f);
        }
    }
    public static void add(Player player, float amount) {
        float current = HashMaps.BONUSES.get(player);
        HashMaps.BONUSES.remove(player);
        HashMaps.BONUSES.put(player, amount+current);
    }
}
