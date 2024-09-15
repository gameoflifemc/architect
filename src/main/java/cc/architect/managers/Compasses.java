package cc.architect.managers;

import cc.architect.objects.Compass;
import cc.architect.objects.Fonts;
import cc.architect.objects.HashMaps;
import cc.architect.records.RoughLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Compasses {
    private static final int ACCURACY = 360;
    private static final int RADIUS = 30;
    private static final int DIAMETER = RADIUS * 2 + 1;
    private static final int SENSITIVITY = 5;
    private static final Component EMPTY_SHORT = Component.text("_");
    private static final Component EMPTY_LONG = Component.text("______");
    private static final Component[] DIRECTIONS = new Component[ACCURACY];
    private static final TextColor[] COLORS = new TextColor[DIAMETER];
    private static final JoinConfiguration JOIN_CONFIGURATION = JoinConfiguration.noSeparators();
    public static void setupLocations(Player p) {
        // prepare rough locations
        List<RoughLocation> roughLocations = new ArrayList<>();
        // add locations based on world
        switch (p.getWorld().getName()) {
            case "village":
                roughLocations.add(new RoughLocation("village","_M_",48,159));
                //roughLocations.add(new RoughLocation("village","_F_",-37,160));
                roughLocations.add(new RoughLocation("village","_B_",20,-37));
                roughLocations.add(new RoughLocation("village","_O_",88,-61));
                roughLocations.add(new RoughLocation("village","_C_",14,-81));
                roughLocations.add(new RoughLocation("village","_H_",11,-113));
                roughLocations.add(new RoughLocation("village","_P_",-14,-75));
                roughLocations.add(new RoughLocation("village","_T_",-12,-118));
                break;
            case "mine":
                roughLocations.add(new RoughLocation("mine","_M_",12,-12));
                roughLocations.add(new RoughLocation("mine","_V_",2,-126));
                break;
            case "farm":
                roughLocations.add(new RoughLocation("farm","_F_",7,-7));
                roughLocations.add(new RoughLocation("mine","_V_",14,-14));
                break;
            case "travel":
                //mine
                double x = p.getX();
                if (30 > x){
                    roughLocations.add(new RoughLocation("travel","_M_",-9,38));
                }
                //farm
                else if (30 < x && x < 150){
                    roughLocations.add(new RoughLocation("travel","_F_",126,57));
                }
                //village
                else if (150 < x){
                    roughLocations.add(new RoughLocation("travel","_V_",175,65));
                }
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
                DIRECTIONS[i] = EMPTY_SHORT;
                continue;
            }
            switch (i) {
                case 0:
                    DIRECTIONS[i] = Component.text("__N__");
                    break;
                case 45:
                    DIRECTIONS[i] = Component.text("_NE_");
                    break;
                case 90:
                    DIRECTIONS[i] = Component.text("__E__");
                    break;
                case 135:
                    DIRECTIONS[i] = Component.text("_SE_");
                    break;
                case 180:
                    DIRECTIONS[i] = Component.text("__S__");
                    break;
                case 225:
                    DIRECTIONS[i] = Component.text("_SW_");
                    break;
                case 270:
                    DIRECTIONS[i] = Component.text("__W__");
                    break;
                case 315:
                    DIRECTIONS[i] = Component.text("_NW_");
                    break;
                default:
                    String value = String.valueOf(i);
                    String filler = switch (value.length()) {
                        case 1 -> "__";
                        case 2 -> "_";
                        default -> "";
                    };
                    DIRECTIONS[i] = Component.text(filler + value + filler);
                    break;
            }
        }
        // create colors
        COLORS[RADIUS] = TextColor.fromHexString("#FFFFFF");
        for (int i = 0; i < RADIUS; i++) {
            int x = i + 1;
            COLORS[i] = TextColor.fromHexString("#0019" + (x <= 9 ? "0" : "") + x);
            int y = RADIUS - i;
            COLORS[RADIUS + x] = TextColor.fromHexString("#0019" + (y <= 9 ? "0" : "") + y);
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
        if(locationMap == null) return;
        // iterate through indexes
        for (int i = 0; i < DIAMETER; i++) {
            // get current index
            int current = indexes[i];
            // save directions
            directions[i] = DIRECTIONS[current].color(COLORS[i]);
            // save locations
            if (i > 5 && i < DIAMETER - 5 && locationMap.containsKey(current)) {
                locations[i] = Component.text(locationMap.get(current).icon());
            } else if (current % SENSITIVITY == 0) {
                locations[i] = EMPTY_LONG;
            } else {
                locations[i] = EMPTY_SHORT;
            }
        }
        // update compass
        Compass compass = HashMaps.COMPASSES.get(p);
        compass.getDirections().name(Component.join(JOIN_CONFIGURATION,directions).font(Fonts.COMPASS));
        compass.getLocations().name(Component.join(JOIN_CONFIGURATION,locations).font(Fonts.COMPASS));
    }
}
