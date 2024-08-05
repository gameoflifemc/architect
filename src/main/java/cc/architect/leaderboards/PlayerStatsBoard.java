package cc.architect.leaderboards;

import me.caps123987.monitorapi.displays.DisplayTextComponent;
import me.caps123987.monitorapi.displays.InteractiveDisplay;
import me.caps123987.monitorapi.displays.RenderMode;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class PlayerStatsBoard {
    public static void createStatsLeaderBoard(Location l) {
        InteractiveDisplay board = new InteractiveDisplay(RenderMode.ALL_PLAYERS_INDIVIDUAL_DISPLAYS);
        setupBoard(board);
        DisplayTextComponent header = new DisplayTextComponent(new Vector(0,-1,0));
        header.setOnSpawnCallback((display, uuid) -> {

            User user = LuckPermsProvider.get().getUserManager().getUser(uuid);
            if (user == null) return;
            String value = user.getCachedData().getMetaData().getMetaValue("money");

            Player player = Bukkit.getPlayer(uuid);

            Bukkit.broadcastMessage(
                    LuckPermsProvider.get().getGroupManager()
                            .getGroup("default")
                            .getNodes()
                            .stream().findFirst().get().getKey());

            String name = "%player_name%";
            name = PlaceholderAPI.setPlaceholders(player, name);
            String ip = "%player_ip%";
            ip = PlaceholderAPI.setPlaceholders(player, ip);

            header.setDisplayText(Arrays.asList(
                    Component.text("Your name is ").append(Component.text(name, Style.style(TextDecoration.BOLD))),
                    Component.text("Your ip is ").append(Component.text(ip, Style.style(TextDecoration.BOLD))),
                    Component.text("Your money is ").append(Component.text(value, TextColor.color(230,230,20),TextDecoration.BOLD))
            ),uuid);
        });
        board.addComponent(header);
        board.create(l);
    }
    public static void setupBoard(InteractiveDisplay board){
        board.setHeader(Component.text("                                                                   "));
        board.enableHeader();


        board.setOnSpawnCallback((display, uuid) -> {
            Player player = Bukkit.getPlayer(uuid);

            String name = "%player_name%";
            name = PlaceholderAPI.setPlaceholders(player, name);
            String ip = "%player_ip%";
            ip = PlaceholderAPI.setPlaceholders(player, ip);

            board.setDisplayText(Arrays.asList(
                    Component.text("Your name is ").append(Component.text(name, Style.style(TextDecoration.BOLD))),
                    Component.text("Your ip is ").append(Component.text(ip, Style.style(TextDecoration.BOLD)))
                ),uuid);
        });
    }
}
