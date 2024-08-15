package cc.architect.leaderboards;

import cc.architect.Utilities;
import cc.architect.leaderboards.stats.StatsCaching;
import me.caps123987.monitorapi.displays.InteractiveDisplay;
import me.caps123987.monitorapi.displays.RenderMode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;

public class GlobalLeaderboard {
    public static void createStatsLeaderBoard(Location l) {
        InteractiveDisplay board = new InteractiveDisplay(RenderMode.ALL_PLAYERS_INDIVIDUAL_DISPLAYS);
        setupMainBoard(board);
        board.create(l);
    }

    public static void setupMainBoard(InteractiveDisplay board) {
        board.setHeader(Component.text("                                                 "));
        board.enableHeader();
        board.setChangeToAllPlayers((display) -> {
            display.setLineWidth(300);
        });

        board.setData("pages", new HashMap<UUID, String>());

        board.setOnSpawnCallback((display,uuid)->{
            HashMap<UUID, String> map = (HashMap<UUID, String>) board.getData("pages");
            map.put(uuid, "score_total");
            renderLeaderboard(board, uuid);
        });
    }

    public static void renderLeaderboard(InteractiveDisplay board, UUID uuid) {
        HashMap<UUID, String> map = (HashMap<UUID, String>) board.getData("pages");
        String page = map.get(uuid);
        board.setChangeToOnePlayer((display)->{
            Component builder = Component.text("");

            int pos = 1;
            for(Pair<String, Integer> tops : StatsCaching.tops.get(page)){
                builder = builder.append(
                    Utilities.getDottedComponent(
                        Component.text(pos+". "+tops.getLeft()+" "),
                        Component.text(tops.getRight(), Style.style(TextDecoration.BOLD)),
                        170)
                );
                builder = builder.appendNewline();
                pos++;
            }

            for(int i = 0; i < 10 - StatsCaching.tops.get(page).size(); i++){
                builder = builder.appendNewline();
            }

            builder = builder.append(
                Utilities.getDottedComponent(
                    Component.text(StatsCaching.positions.get(page).get(uuid).getRight()+". " +getPlayer(uuid).getName()+" ", Style.style(TextDecoration.BOLD)),
                    Component.text(StatsCaching.positions.get(page).get(uuid).getLeft(), Style.style(TextDecoration.BOLD)),
                    170)
            );

            display.text(builder);
        },uuid);
    }
}
