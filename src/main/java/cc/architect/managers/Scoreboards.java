package cc.architect.managers;

import cc.architect.objects.ResponseList;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.concurrent.atomic.AtomicInteger;

import static cc.architect.managers.Dialogues.responseLists;
import static org.bukkit.Bukkit.getScoreboardManager;

public class Scoreboards {
    public static void createObjective(Player p) {
        // get scoreboard
        Scoreboard scoreboard = getScoreboardManager().getNewScoreboard();
        // create objective
        Objective objective = scoreboard.registerNewObjective(p.toString(),Criteria.DUMMY,Component.empty());
        // set objective settings
        objective.numberFormat(NumberFormat.blank());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        // set player's scoreboard
        p.setScoreboard(scoreboard);
    }
    public static void showResponses(Player p) {
        // get response list
        ResponseList responseList = responseLists.get(p);
        // get objective
        Objective objective = p.getScoreboard().getObjective(p.toString());
        if (objective == null) {
            return;
        }
        // initialize counter
        AtomicInteger counter = new AtomicInteger(0);
        // add responses to objective
        for (int i = 0; i < responseList.size(); i++) {
            // get and increment counter
            int count = counter.getAndAdd(3);
            // get components
            Component[] components = responseList.getComponents(i);
            // create scores
            for (int j = 0; j < 3; j++) {
                // prepare values
                int scoreValue = count + j;
                // create score
                Score score = objective.getScore(String.valueOf(scoreValue));
                // set score value
                score.setScore(scoreValue);
                // set score name
                score.customName(components[j]);
            }
        }
    }
}
