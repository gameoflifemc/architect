package cc.architect.managers;

import cc.architect.objects.Compass;
import cc.architect.objects.Fonts;
import cc.architect.objects.HashMaps;
import cc.architect.objects.RoughLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Compasses {
    private static final int ACCURACY = 360;
    private static final int RADIUS = 14;
    private static final int DIAMETER = RADIUS * 2 + 1;
    private static final int SENSITIVITY = 5;
    private static final String[] DIRECTIONS = new String[ACCURACY];
    private static final TextColor[] COLORS = new TextColor[DIAMETER];
    private static final Component EMPTY_SHORT = Component.text("__");
    private static final Component EMPTY_LONG = Component.text("______");
    public static void setupLocations(Player p) {
        // prepare rough locations
        List<RoughLocation> roughLocations = new ArrayList<>();
        // add locations based on world
        switch (p.getWorld().getName()) {
            case "village":
                roughLocations.add(new RoughLocation("village","_M_",0,0));
                break;
            case "mine":
                roughLocations.add(new RoughLocation("mine","_M_",0,0));
                break;
            case "farm":
                roughLocations.add(new RoughLocation("farm","_M_",0,0));
                break;
            case "dream":
                roughLocations.add(new RoughLocation("dream","_M_",0,0));
                break;
            case "travel":
                roughLocations.add(new RoughLocation("travel","_M_",0,0));
                break;
        }
        HashMaps.WORLD_LOCATIONS.put(p,roughLocations);
        // update values for the first time
        Compasses.updateLocations(p);
    }
    public static void setupValues() {
        // create directions
        for (int i = 0; i < ACCURACY; i++) {
            if (i % SENSITIVITY != 0) {
                DIRECTIONS[i] = "__";
                continue;
            }
            switch (i) {
                case 0:
                    DIRECTIONS[i] = "__N__";
                    break;
                case 45:
                    DIRECTIONS[i] = "_NE_";
                    break;
                case 90:
                    DIRECTIONS[i] = "__E__";
                    break;
                case 135:
                    DIRECTIONS[i] = "_SE_";
                    break;
                case 180:
                    DIRECTIONS[i] = "__S__";
                    break;
                case 225:
                    DIRECTIONS[i] = "_SW_";
                    break;
                case 270:
                    DIRECTIONS[i] = "__W__";
                    break;
                case 315:
                    DIRECTIONS[i] = "_NW_";
                    break;
                default:
                    String value = String.valueOf(i);
                    String filler = switch (value.length()) {
                        case 1 -> "__";
                        case 2 -> "_";
                        default -> "";
                    };
                    DIRECTIONS[i] = filler + value + filler;
                    break;
            }
        }
        // create colors
        COLORS[RADIUS] = TextColor.fromHexString("#FFFFFF");
        for (int i = 0; i < RADIUS; i++) {
            int x = i + 1;
            String filler;
            if (x < 10) {
                filler = "0";
            } else {
                filler = "";
            }
            COLORS[i] = TextColor.fromHexString("#0000" + filler + x);
            COLORS[RADIUS + x] = TextColor.fromHexString("#0000" + filler + (RADIUS - i));
        }
    }
    public static void updateLocations(Player p) {
        // prepare locations
        HashMap<Integer,RoughLocation> locations = new HashMap<>();
        // iterate through locations
        for (RoughLocation location : HashMaps.WORLD_LOCATIONS.get(p)) {
            // calculate yaw
            int yaw = (int) Math.toDegrees(Math.atan2(location.z() - Math.round(p.getZ()),location.x() - Math.round(p.getX()))) - 90;
            // handle overflow
            if (yaw < 0) {
                yaw += ACCURACY;
            }
            // save location
            locations.put(yaw,location);
        }
        // save locations
        HashMaps.ROTATION_LOCATIONS.put(p,locations);
    }
    public static void updateValues(Player p) {
        // prepare indexes
        int[] indexes = new int[DIAMETER];
        // calculate indexes
        for (int i = -RADIUS; i <= RADIUS; i++) {
            // calculate next index
            int next = Math.round(p.getYaw()) + i;
            // handle overflow
            if (next >= ACCURACY) {
                next -= ACCURACY;
            } else if (next < 0) {
                next += ACCURACY;
            }
            // save index
            indexes[i + RADIUS] = next;
        }
        // prepare components
        Component[] directions = new Component[DIAMETER];
        Component[] locations = new Component[DIAMETER];
        // get location map
        HashMap<Integer,RoughLocation> locationMap = HashMaps.ROTATION_LOCATIONS.get(p);
        // iterate through indexes
        for (int i = 0; i < DIAMETER; i++) {
            // get current index
            int current = indexes[i];
            // save directions
            directions[i] = Component.text(DIRECTIONS[current]).color(COLORS[i]);
            // save locations
            if (i != 0 && i != DIAMETER - 1 && locationMap.containsKey(current)) {
                locations[i] = Component.text(locationMap.get(current).icon());
            } else if (current % SENSITIVITY == 0) {
                locations[i] = EMPTY_LONG;
            } else {
                locations[i] = EMPTY_SHORT;
            }
        }
        // update compass
        Compass compass = HashMaps.COMPASSES.get(p);
        JoinConfiguration config = JoinConfiguration.noSeparators();
        compass.getDirections().name(Component.join(config,directions).font(Fonts.COMPASS));
        compass.getLocations().name(Component.join(config,locations).font(Fonts.COMPASS));
    }
}
