package cc.architect.leaderboards.stats;

import java.util.Collections;
import java.util.List;

public class StatsSorter {
    public static List<PlayerStatsHolder> listTop(List<PlayerStatsHolder> stats, int amount, StatComparator comparator) {
        for (int iteration = 0; iteration < amount; iteration++){
            int iMax = iteration;
            for (int i = iteration+1; i < stats.size()-1;i++){
                if(comparator.get(stats.get(i)) > comparator.get(stats.get(iMax))){
                    iMax = i;
                }
            }
            Collections.swap(stats,iteration,iMax);
        }
        return stats;
    }
    public static List<PlayerStatsHolder> listAll(List<PlayerStatsHolder> stats, StatComparator comparator) {
        stats.sort((a,b) -> comparator.get(b) - comparator.get(a));
        return stats;
    }
}
