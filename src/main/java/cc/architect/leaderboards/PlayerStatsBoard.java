package cc.architect.leaderboards;

import cc.architect.Utilities;
import cc.architect.leaderboards.stats.PlayerStatsHolder;
import me.caps123987.monitorapi.displays.DisplayTextComponent;
import me.caps123987.monitorapi.displays.InteractiveDisplay;
import me.caps123987.monitorapi.displays.RenderMode;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

import java.util.Arrays;

public class PlayerStatsBoard {
    public static void createStatsLeaderBoard(Location l) {
        InteractiveDisplay board = new InteractiveDisplay(RenderMode.ALL_PLAYERS_INDIVIDUAL_DISPLAYS);
        setupBoard(board);
        DisplayTextComponent header = new DisplayTextComponent(new Vector(0,1.35,0.05));
        header.setOnSpawnCallback((display, uuid) -> {
            header.setDisplayText(Component.text("Tvoje statistiky", Style.style(TextDecoration.BOLD,TextColor.color(220,220,20))),uuid);
            header.setChangeToAllPlayers((display1, uuid1) -> display1.setTransformation(
                new Transformation(
                    display1.getTransformation().getTranslation(),
                    display1.getTransformation().getLeftRotation(),
                    new Vector3f(2.5f,2.5f,2.5f),
                    display1.getTransformation().getRightRotation()
                )
            ));
        });
        board.addComponent(header);
        board.create(l);
    }

    public static void setupBoard(InteractiveDisplay board){
        board.setHeader(Component.text("                                             "));
        board.enableHeader();
        board.setChangeToAllPlayers((display)->{display.setLineWidth(300);});

        board.setOnSpawnCallback((display, uuid) -> {
            PlayerStatsHolder stats = new PlayerStatsHolder(uuid);

            board.setDisplayText(Arrays.asList(
                    Utilities.getDottedComponent(Component.text("Celkové score "), Component.text(stats.getScore_total(), Style.style(TextDecoration.BOLD)), 160),
                    Utilities.getDottedComponent(Component.text("Celkový počet emeraldů "), Component.text(stats.getEmeralds_total(), Style.style(TextDecoration.BOLD)), 160),
                    Utilities.getDottedComponent(Component.text("Dnešní score "), Component.text(stats.getScore_daily(), Style.style(TextDecoration.BOLD)), 160),
                    Utilities.getDottedComponent(Component.text("Dnešní počet emeraldů "), Component.text(stats.getEmeralds_daily(), Style.style(TextDecoration.BOLD)), 160),
                    Utilities.getDottedComponent(Component.text("Počet strávených dnů "), Component.text(stats.getDays_total(), Style.style(TextDecoration.BOLD)), 160)
                ),uuid);
        });
    }
}
