package cc.architect.leaderboards;

import cc.architect.Utilities;
import cc.architect.leaderboards.stats.StatsCaching;
import me.caps123987.monitorapi.displays.DisplayButtonComponent;
import me.caps123987.monitorapi.displays.DisplayTextComponent;
import me.caps123987.monitorapi.displays.InteractiveDisplay;
import me.caps123987.monitorapi.displays.RenderMode;
import me.caps123987.monitorapi.messages.DisplayMessages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;

public class GlobalLeaderboard {

    public static final String[] pageTypes = {"score","emeralds","investments","loan"};
    public static void createStatsLeaderBoard(Location l) {
        InteractiveDisplay board = new InteractiveDisplay(RenderMode.ALL_PLAYERS_INDIVIDUAL_DISPLAYS);
        setupMainBoard(board);
        DisplayButtonComponent durationButton = new DisplayButtonComponent(new Vector(0,-.75,0));
        setupDurationButton(board, durationButton, "duration","_highest","_daily",Messages.durationTotal,Messages.durationDaily);

        DisplayButtonComponent typeButton = new DisplayButtonComponent(new Vector(4,1,0));
        setupTypeButton(board, typeButton, "type");

        DisplayTextComponent header = new DisplayTextComponent(new Vector(0,3,0.05));
        header.setOnSpawnCallback((display, uuid) -> {
            header.setDisplayText(Component.text("Leaderboard", Style.style(TextDecoration.BOLD,TextColor.color(220,220,20))),uuid);
            header.setChangeToAllPlayers((display1, uuid1) -> display1.setTransformation(
                    new Transformation(
                            display1.getTransformation().getTranslation(),
                            display1.getTransformation().getLeftRotation(),
                            new Vector3f(2.5f,2.5f,2.5f),
                            display1.getTransformation().getRightRotation()
                    )
            ));
        });

        DisplayMessages.CLICK_COOLDOWN = Component.text("Prosím zpomal");
        board.addComponent(durationButton);
        board.addComponent(typeButton);
        board.addComponent(header);
        board.create(l);
    }

    public static void setupMainBoard(InteractiveDisplay board) {
        board.setChangeToAllPlayers((display) -> {
            display.setLineWidth(3000);
        });

        board.setData("type", new HashMap<UUID, String>());
        board.setData("duration", new HashMap<UUID, String>());

        board.setOnSpawnCallback((display,uuid)->{
            HashMap<UUID, String> types = (HashMap<UUID, String>) board.getData("type");
            types.put(uuid, "0");
            HashMap<UUID, String> duration = (HashMap<UUID, String>) board.getData("duration");
            duration.put(uuid, "_highest");
            renderLeaderboard(board, uuid);
        });
    }

    public static void renderLeaderboard(InteractiveDisplay board, UUID uuid) {
        HashMap<UUID, String> types = (HashMap<UUID, String>) board.getData("type");
        String type = types.get(uuid);
        HashMap<UUID, String> durations = (HashMap<UUID, String>) board.getData("duration");
        String duration = durations.get(uuid);
        String page = pageTypes[Integer.parseInt(type)]+duration;

        board.setChangeToOnePlayer((display)->{
            Component builder = Component.text("");

            int pos = 1;
            List<Pair<String, Integer>> topAll = StatsCaching.tops.get(page);
            if(topAll != null){
                for(Pair<String, Integer> tops : topAll){
                    builder = builder.append(
                        Utilities.getDottedComponent(
                            Component.text(pos+". "+tops.getLeft()+" "),
                            Component.text(tops.getRight(), Style.style(TextDecoration.BOLD)),
                            250)
                    );
                    builder = builder.appendNewline();
                    pos++;
                }

                for(int i = 0; i < 10 - topAll.size(); i++){
                    builder = builder.appendNewline();
                }
            }else{
                for(int i = 0; i < 10; i++){
                    builder = builder.append(Utilities.getDottedComponent(
                            Component.text(pos+". Vyčkej do dalšího obnovení scoreboardu "),
                            Component.text("", Style.style(TextDecoration.BOLD)),
                            250)).appendNewline();
                    pos++;
                }
            }

            Map<UUID,Pair<Integer, Integer>> positions = StatsCaching.positions.get(page);
            if(positions == null){
                display.text(builder);
                return;
            }
            Pair<Integer, Integer> stats = positions.getOrDefault(uuid, Pair.of(0,positions.size()));

            builder = builder.append(
                Utilities.getDottedComponent(
                    Component.text(stats.getRight()+". " +getPlayer(uuid).getName()+" ", Style.style(TextDecoration.BOLD)),
                    Component.text(stats.getLeft(), Style.style(TextDecoration.BOLD)),
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
        comp.getComponentInteraction().setInteractionWidth(2.5f);
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
    public static void setupTypeButton(InteractiveDisplay board, DisplayButtonComponent comp, String mapName) {
        comp.setOnSpawnCallback((display,uuid)->{
            comp.setChangeToOnePlayer((display1)->{
                display1.text(Messages.typeScore);
            },uuid);
        });

        comp.enableCooldown();
        comp.getComponentInteraction().setInteractionWidth(1.5f);
        comp.setCooldownTime(5);
        comp.onClick((display, event)->{
            UUID uuid = event.getPlayer().getUniqueId();
            HashMap<UUID, String> types = (HashMap<UUID, String>) board.getData(mapName);

            int idx = Integer.parseInt(types.get(uuid));
            switch (idx){
                case 0:
                    types.put(uuid, "1");
                    break;
                case 1:
                    types.put(uuid, "2");
                    break;
                case 2:
                    types.put(uuid, "3");
                    break;
                case 3:
                    types.put(uuid, "0");
                    break;
            }
            //board.setData(mapName, types);

            comp.setChangeToOnePlayer((display1)->{
                switch (idx){
                    case 0:
                        display1.text(Messages.typeEmeralds);
                        break;
                    case 1:
                        display1.text(Messages.typeInvestments);
                        break;
                    case 2:
                        display1.text(Messages.typeLoans);
                        break;
                    case 3:
                        display1.text(Messages.typeScore);
                        break;
                }
            },uuid);

            renderLeaderboard(board, uuid);
        });
    }

    public class Messages{
        public static Component durationTotal = Component.text("Celkové výsledky", TextColor.color(255, 230,0))
                .appendNewline().append(Component.text("Průměrné výsledky", TextColor.color(125,125,125)));
        public static Component durationDaily = Component.text("Celkové výsledky", TextColor.color(125,125,125))
                .appendNewline().append(Component.text("Průměrné výsledky", TextColor.color(255, 230,0)));
        public static Component typeScore = Component.text("Score", TextColor.color(255, 230,0))
                .appendNewline().append(Component.text("Emeraldy", TextColor.color(125,125,125)))
                .appendNewline().append(Component.text("Investice", TextColor.color(125,125,125)))
                .appendNewline().append(Component.text("Půjčky", TextColor.color(125,125,125)));
        public static Component typeEmeralds = Component.text("Score", TextColor.color(125,125,125))
                .appendNewline().append(Component.text("Emeraldy", TextColor.color(255, 230,0)))
                .appendNewline().append(Component.text("Investice", TextColor.color(125,125,125)))
                .appendNewline().append(Component.text("Půjčky", TextColor.color(125,125,125)));
        public static Component typeInvestments = Component.text("Score", TextColor.color(125,125,125))
                .appendNewline().append(Component.text("Emeraldy", TextColor.color(125,125,125)))
                .appendNewline().append(Component.text("Investice", TextColor.color(255, 230,0)))
                .appendNewline().append(Component.text("Půjčky", TextColor.color(125,125,125)));
        public static Component typeLoans = Component.text("Score", TextColor.color(125,125,125))
                .appendNewline().append(Component.text("Emeraldy", TextColor.color(125,125,125)))
                .appendNewline().append(Component.text("Investice", TextColor.color(125,125,125)))
                .appendNewline().append(Component.text("Půjčky", TextColor.color(255, 230,0)));
    }
}
