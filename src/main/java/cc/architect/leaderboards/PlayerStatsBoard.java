package cc.architect.leaderboards;

import cc.architect.Utilities;
import cc.architect.commands.money.Loan;
import cc.architect.commands.money.Savings;
import cc.architect.commands.money.investments.InvestmentGlobal;
import cc.architect.leaderboards.stats.PlayerStatsHolder;
import me.caps123987.monitorapi.displays.DisplayButtonComponent;
import me.caps123987.monitorapi.displays.DisplayTextComponent;
import me.caps123987.monitorapi.displays.InteractiveDisplay;
import me.caps123987.monitorapi.displays.RenderMode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

import java.util.Arrays;

import static org.bukkit.Bukkit.getPlayer;


public class PlayerStatsBoard {
    public static void createStatsLeaderBoard(Location l) {
        InteractiveDisplay board = new InteractiveDisplay(RenderMode.ALL_PLAYERS_INDIVIDUAL_DISPLAYS);
        setupBoard(board);
        DisplayTextComponent header = new DisplayTextComponent(new Vector(0,2.95,0.05));
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
        DisplayButtonComponent emeraldsInfo = new DisplayButtonComponent(new Vector(-3.4,2.25,0.05));
        emeraldsInfo.setOnSpawnCallback((display, uuid) -> {
            emeraldsInfo.setDisplayText(Component.text("ⓘ"),uuid);
        });
        emeraldsInfo.onClick((display, event) -> {
            event.getPlayer().sendMessage(Component.text("Počet emeraldů se počítá takto: \n Emeraldy v inventáři + Spoření + Investice - Půjčky \nPokud budeš kdykoli v mínusu tak ti tabulka ukáže hodnotu 0"));
        });
        board.addComponent(header);
        board.addComponent(emeraldsInfo);
        board.create(l);
    }

    public static void setupBoard(InteractiveDisplay board){
        board.setChangeToAllPlayers((display)-> display.setLineWidth(300));

        board.setOnSpawnCallback((display, uuid) -> {
            PlayerStatsHolder stats = new PlayerStatsHolder(uuid);

            board.setDisplayText(Arrays.asList(
                    Utilities.getDottedComponent(Component.text("Celkové score "), Component.text(stats.getScore_total(), Style.style(TextDecoration.BOLD)), 250),
                    Utilities.getDottedComponent(Component.text("Průměrné score "), Component.text(stats.getScore_daily(), Style.style(TextDecoration.BOLD)), 250),
                    Utilities.getDottedComponent(Component.text("Celkový počet emeraldů "), Component.text(stats.getEmeralds_total(), Style.style(TextDecoration.BOLD)), 250),
                    Utilities.getDottedComponent(Component.text("Průměrný počet emeraldů "), Component.text(stats.getEmeralds_daily(), Style.style(TextDecoration.BOLD)), 250),
                    Utilities.getDottedComponent(Component.text("Aktuální hodnota spoření "), Component.text(Savings.get(uuid), Style.style(TextDecoration.BOLD)), 250),
                    Utilities.getDottedComponent(Component.text("Aktuální hodnota investic "), Component.text(InvestmentGlobal.countAllInvestments(uuid), Style.style(TextDecoration.BOLD)), 250),
                    Utilities.getDottedComponent(Component.text("Celková hodnota investic "), Component.text(stats.getInvestice_total(), Style.style(TextDecoration.BOLD)), 250),
                    Utilities.getDottedComponent(Component.text("Průměrná hodnota investic "), Component.text(stats.getInvestice_daily(), Style.style(TextDecoration.BOLD)), 250),
                    Utilities.getDottedComponent(Component.text("Aktuální hodnota půjček "), Component.text(Loan.countAllAmount(uuid), Style.style(TextDecoration.BOLD)), 250),
                    Utilities.getDottedComponent(Component.text("Celková hodnota půjček "), Component.text(stats.getPujcky_total(), Style.style(TextDecoration.BOLD)), 250),
                    Utilities.getDottedComponent(Component.text("Průměrná hodnota půjček "), Component.text(stats.getPujcky_daily(), Style.style(TextDecoration.BOLD)), 250),
                    Utilities.getDottedComponent(Component.text("Počet strávených dnů "), Component.text(stats.getDays_total(), Style.style(TextDecoration.BOLD)), 250)
                ),uuid);
        });
    }
}
