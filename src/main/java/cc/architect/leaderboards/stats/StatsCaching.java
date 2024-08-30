package cc.architect.leaderboards.stats;

import cc.architect.Architect;
import cc.architect.managers.Meta;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.ToolComponent;

import java.util.*;

public class StatsCaching {

    /**value, pos**/
    public static Map<String, Map<UUID,Pair<Integer, Integer>>> positions = new HashMap<>();
    public static Map<String, List<Pair<String, Integer>>> tops = new HashMap<>();

    public static List<Pair<String, StatComparator>> comparators = List.of(
        Pair.of("score_total", PlayerStatsHolder::getScore_total),
        Pair.of("emeralds_total", PlayerStatsHolder::getEmeralds_total),
        Pair.of("days_total", PlayerStatsHolder::getDays_total),
        Pair.of("score_daily", PlayerStatsHolder::getScore_daily),
        Pair.of("emeralds_daily", PlayerStatsHolder::getEmeralds_daily),
        Pair.of("investice_total", PlayerStatsHolder::getInvestice_total),
        Pair.of("investice_daily", PlayerStatsHolder::getInvestice_daily),
        Pair.of("pujcky_total", PlayerStatsHolder::getPujcky_total),
        Pair.of("pujcky_daily", PlayerStatsHolder::getPujcky_daily)
    );
    public static void cacheStats() {
        Architect.SCHEDULER.runTaskAsynchronously(Architect.PLUGIN,()->{
            positions.clear();
            tops.clear();
            List<UUID> players = Players.getAllPlayers();
            List<PlayerStatsHolder> statsHolders = new ArrayList<>();

            for (UUID player : players) {
                statsHolders.add(new PlayerStatsHolder(player));
            }

            for (int i = 0; i < 2000; i++) {
                statsHolders.add(new PlayerStatsHolder(5000));
            }

            for(Pair<String, StatComparator> pair : comparators){
                addPostions(StatsSorter.listAll(statsHolders, pair.getRight()),pair.getRight(),pair.getLeft());
                addTops(statsHolders, pair.getRight(), pair.getLeft(), 10);
            }
        });
    }
    public static void addPostions(List<PlayerStatsHolder> statsHolders, StatComparator comp, String name) {
        Map<UUID, Pair<Integer, Integer>> positionsMap = new HashMap<>();
        for(int i = 0; i < statsHolders.size(); i++) {
            positionsMap.put(statsHolders.get(i).getUuid(), Pair.of(comp.get(statsHolders.get(i)),statsHolders.indexOf(statsHolders.get(i))+1));
        }
        positions.put(name, positionsMap);
    }

    public static void addTops(List<PlayerStatsHolder> statsHolders, StatComparator comp, String name, int size) {
        List<Pair<String, Integer>> topsList = new ArrayList<>();
        for (int i = 0; i < size && i < statsHolders.size(); i++) {
            topsList.add(Pair.of(Meta.toUser(statsHolders.get(i).getUuid()).getUsername(), comp.get(statsHolders.get(i))));
        }
        tops.put(name, topsList);
    }
}
