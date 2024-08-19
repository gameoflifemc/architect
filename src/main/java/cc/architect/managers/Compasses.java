package cc.architect.managers;

import cc.architect.objects.Fonts;
import cc.architect.objects.HashMaps;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

public class Compasses {
    private static final String[] VALUES = new String[360];
    private static final int ACCURACY = 360;
    private static final int RADIUS = 15;
    private static final int DIAMETER = RADIUS * 2 + 1;
    private static final Component SEPARATOR = Component.text("$");
    private static final TextColor[] COLORS = new TextColor[31];
    public static void prepare() {
        for (int i = 0; i < ACCURACY; i++) {
            if (i % 5 != 0) {
                VALUES[i] = "      ";
                continue;
            }
            switch (i) {
                case 0:
                    VALUES[i] = "  N  ";
                    break;
                case 45:
                    VALUES[i] = " NE ";
                    break;
                case 90:
                    VALUES[i] = "  E  ";
                    break;
                case 135:
                    VALUES[i] = " SE ";
                    break;
                case 180:
                    VALUES[i] = "  S  ";
                    break;
                case 225:
                    VALUES[i] = " SW ";
                    break;
                case 270:
                    VALUES[i] = "  W  ";
                    break;
                case 315:
                    VALUES[i] = " NW ";
                    break;
                default:
                    String value = String.valueOf(i);
                    String filler = switch (value.length()) {
                        case 1 -> "  ";
                        case 2 -> " ";
                        default -> "";
                    };
                    VALUES[i] = filler + value + filler;
                    break;
            }
        }
        COLORS[15] = TextColor.fromHexString("#FFFFFF");
        for (int i = 0; i < 15; i++) {
            int x = i + 1;
            String filler;
            if (x < 10) {
                filler = "0";
            } else {
                filler = "";
            }
            COLORS[i] = TextColor.fromHexString("#0000" + filler + x);
            COLORS[16 + i] = TextColor.fromHexString("#0000" + filler + (16 - x));
        }
    }
    public static void create(Player p) {
        BossBar compass = BossBar.bossBar(
            Component.empty(),
            BossBar.MAX_PROGRESS,
            BossBar.Color.WHITE,
            BossBar.Overlay.PROGRESS
        );
        compass.addViewer(p);
        HashMaps.COMPASSES.put(p, compass);
    }
    public static void update(Player p, int yaw) {
        // prepare components
        Component[] components = new Component[DIAMETER];
        // prepare indexes
        int[] indexes = new int[DIAMETER];
        // calculate indexes
        for (int i = -RADIUS; i <= RADIUS; i++) {
            // calculate next index
            int next = yaw + i;
            // handle overflow
            if (next >= ACCURACY) {
                next -= ACCURACY;
            } else if (next < 0) {
                next += ACCURACY;
            }
            // save index
            indexes[i + RADIUS] = next;
        }
        // iterate through indexes
        for (int i = 0; i < DIAMETER; i++) {
            // save component
            components[i] = Component.text(VALUES[indexes[i]]).color(COLORS[i]);
        }
        // update compass
        HashMaps.COMPASSES.get(p).name(Component.join(JoinConfiguration.separator(SEPARATOR),components).font(Fonts.COMPASS));
    }
    public static void remove(Player p) {
        HashMaps.COMPASSES.remove(p);
    }
}
