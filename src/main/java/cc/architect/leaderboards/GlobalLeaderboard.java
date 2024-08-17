package cc.architect.leaderboards;

import cc.architect.Utilities;
import cc.architect.leaderboards.stats.StatsCaching;
import me.caps123987.monitorapi.displays.DisplayButtonComponent;
import me.caps123987.monitorapi.displays.InteractiveDisplay;
import me.caps123987.monitorapi.displays.RenderMode;
import me.caps123987.monitorapi.messages.DisplayMessages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;

public class GlobalLeaderboard {
    public static void createStatsLeaderBoard(Location l) {
        InteractiveDisplay board = new InteractiveDisplay(RenderMode.ALL_PLAYERS_INDIVIDUAL_DISPLAYS);
        setupMainBoard(board);
        DisplayButtonComponent durationButton = new DisplayButtonComponent(new Vector(-2.2,-.75,0));
        setupDurationButton(board, durationButton, "duration","_total","_daily",Messages.durationTotal,Messages.durationDaily);

        DisplayButtonComponent typeButton = new DisplayButtonComponent(new Vector(2.2,-.75,0));
        setupDurationButton(board, typeButton, "type","score","emeralds",Messages.typeScore,Messages.typeEmeralds);

        DisplayMessages.CLICK_COOLDOWN = Component.text("Prosím zpomal");
        board.addComponent(durationButton);
        board.addComponent(typeButton);
        board.create(l);
    }

    public static void setupMainBoard(InteractiveDisplay board) {
        board.setHeader(Component.text("                                                 "));
        board.enableHeader();
        board.setChangeToAllPlayers((display) -> {
            display.setLineWidth(3000);
        });

        board.setData("type", new HashMap<UUID, String>());
        board.setData("duration", new HashMap<UUID, String>());

        board.setOnSpawnCallback((display,uuid)->{
            HashMap<UUID, String> types = (HashMap<UUID, String>) board.getData("type");
            types.put(uuid, "score");
            HashMap<UUID, String> duration = (HashMap<UUID, String>) board.getData("duration");
            duration.put(uuid, "_total");
            renderLeaderboard(board, uuid);
        });
    }

    public static void renderLeaderboard(InteractiveDisplay board, UUID uuid) {
        HashMap<UUID, String> types = (HashMap<UUID, String>) board.getData("type");
        String type = types.get(uuid);
        HashMap<UUID, String> durations = (HashMap<UUID, String>) board.getData("duration");
        String duration = durations.get(uuid);
        String page = type+duration;

        board.setChangeToOnePlayer((display)->{
            Component builder = Component.text("");

            int pos = 1;
            for(Pair<String, Integer> tops : StatsCaching.tops.get(page)){
                builder = builder.append(
                    Utilities.getDottedComponent(
                        Component.text(pos+". "+tops.getLeft()+" "),
                        Component.text(tops.getRight(), Style.style(TextDecoration.BOLD)),
                        250)
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
                    250)
            );

            display.text(builder);
        },uuid);
    }

    public static void setupDurationButton(InteractiveDisplay board, DisplayButtonComponent comp, String mapName
            , String switch1, String switch2, Component switch1C, Component switch2C) {
        comp.setOnSpawnCallback((display,uuid)->{
            comp.setChangeToOnePlayer((display1)->{
                display1.text(switch1C);
            },uuid);
        });

        comp.enableCooldown();
        comp.setCooldownTime(5);
        comp.onClick((display, event)->{
            UUID uuid = event.getPlayer().getUniqueId();
            HashMap<UUID, String> durations = (HashMap<UUID, String>) board.getData(mapName);

            String duration = durations.get(uuid);
            if(duration.equals(switch1)){
                durations.put(uuid, switch2);
            } else {
                durations.put(uuid, switch1);
            }
            board.setData(mapName, durations);

            comp.setChangeToOnePlayer((display1)->{
                if(duration.equals(switch1)){
                    display1.text(switch2C);
                } else {
                    display1.text(switch1C);
                }
            },uuid);

            renderLeaderboard(board, uuid);
        });
    }

    public class Messages{
        public static Component durationTotal = Component.text("Klikni pro změnu na ").append(Component.text("denní", TextColor.color(255, 230,0)))
                .appendNewline().append(Component.text("(aktuálně vidíš celkové výsledky)", TextColor.color(125,125,125)));
        public static Component durationDaily = Component.text("Klikni pro změnu na ").append(Component.text("celkové", TextColor.color(255, 230,0)))
                .appendNewline().append(Component.text("(aktuálně vidíš denní výsledky)", TextColor.color(125,125,125)));
        public static Component typeScore = Component.text("Klikni pro změnu na ").append(Component.text("emeraldy", TextColor.color(255, 230,0)))
                .appendNewline().append(Component.text("(aktuálně vidíš výsledky score)", TextColor.color(125,125,125)));
        public static Component typeEmeralds = Component.text("Klikni pro změnu na ").append(Component.text("score", TextColor.color(255, 230,0)))
                .appendNewline().append(Component.text("(aktuálně vidíš výsledky emeraldů)", TextColor.color(125,125,125)));
    }
}
