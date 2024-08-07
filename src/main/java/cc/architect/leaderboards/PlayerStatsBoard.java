package cc.architect.leaderboards;

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
        DisplayTextComponent header = new DisplayTextComponent(new Vector(0,2.5,0.1));
        header.setOnSpawnCallback((display, uuid) -> {
            header.setDisplayText(Component.text("Your Stats", Style.style(TextDecoration.BOLD,TextColor.color(220,220,20))),uuid);
            header.setChangeToAllPlayers((display1, uuid1) -> display1.setTransformation(
                new Transformation(
                    display1.getTransformation().getTranslation(),
                    display1.getTransformation().getLeftRotation(),
                    new Vector3f(2f,2f,2f),
                    display1.getTransformation().getRightRotation()
                )
            ));
        });
        board.addComponent(header);
        board.create(l);
    }

    public static void setupBoard(InteractiveDisplay board){
        board.setHeader(Component.text("                                                                   "));
        board.enableHeader();


        board.setOnSpawnCallback((display, uuid) -> {
            Player player = Bukkit.getPlayer(uuid);

            PlayerStatsHolder stats = new PlayerStatsHolder(uuid);

            String name = "%player_name%";
            name = PlaceholderAPI.setPlaceholders(player, name);
            String ip = "%player_ip%";
            ip = PlaceholderAPI.setPlaceholders(player, ip);

            board.setDisplayText(Arrays.asList(
                    Component.text("Your name is ").append(Component.text(name, Style.style(TextDecoration.BOLD))),
                    Component.text("Your uuid is ").append(Component.text(uuid.toString(), Style.style(TextDecoration.BOLD))),
                    Component.text("Your ip is ").append(Component.text(ip, Style.style(TextDecoration.BOLD))),
                    Component.text("Your money is ").append(Component.text(stats.getMoney(), TextColor.color(230,230,20),TextDecoration.BOLD)),
                    Component.text("Your score is ").append(Component.text(stats.getScore(), TextColor.color(230,230,20),TextDecoration.BOLD)),
                    Component.text("Your networth is ").append(Component.text(stats.getNetWorth(), TextColor.color(230,230,20),TextDecoration.BOLD)),
                    Component.text("Your debt is ").append(Component.text(stats.getDebt(), TextColor.color(230,20,20),TextDecoration.BOLD))
                ),uuid);
        });
    }
}
