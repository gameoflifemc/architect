package cc.architect.managers;

import cc.architect.objects.Fonts;
import cc.architect.objects.HashMaps;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Compasses {
    private static final List<Integer> VALUES = new ArrayList<>();
    private static final List<Integer> INDEX = new ArrayList<>();
    private static final int SHOWN = 11;
    private static final int ACCURACY = 360;
    public static void prepare() {
        for (int i = 0; i < ACCURACY; i++) {
            VALUES.add(i);
            if (i < SHOWN) {
                INDEX.add(i);
            }
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
    public static void update(Player p, int direction) {
        // prepare components
        Component[] components = new Component[SHOWN];
        // iterate through indexes
        for (int i = 0; i < SHOWN; i++) {
            // increment index
            int next = INDEX.get(i) + direction;
            if (next >= ACCURACY) {
                next -= ACCURACY;
            } else if (next < 0) {
                next += ACCURACY;
            }
            int value = VALUES.get(next);
            String filler;
            if (value >= 100) {
                filler = "";
            } else if (value >= 10) {
                filler = "0";
            } else {
                filler = "00";
            }
            // get corresponding value
            components[i] = Component.text(filler + value + "$").font(Fonts.COMPASS.get(i));
            // save index
            INDEX.set(i, next);
        }
        // update compass
        HashMaps.COMPASSES.get(p).name(Component.text().append(components));
    }
    public static void remove(Player p) {
        HashMaps.COMPASSES.remove(p);
    }
}
