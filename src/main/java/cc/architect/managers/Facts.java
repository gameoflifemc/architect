package cc.architect.managers;

import cc.architect.Architect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Facts {
    public static final String[] FACTS = {
        "eif5GXYBcwkJK3P",
        "Y2n3yM3Mi5Dyagc",
        "vhpXgbQKTMeQ5w4",
        "3JtA3527BVZbesM",
        "1IpI56QCdApGD33",
        "mm22LuT8w4c8hjl",
        "I3O438Bqx90imFk",
        "bYXFPC4qNA5Ox6q",
        "rtcDFAPy90vcec8",
        "wK5dBo4vc1DkOrc",
        "WhhEyFKTw9p7x7U"
    };
    public static void synchronize(Player p) {
        // synchronize facts
        for (String fact : FACTS) {
            // get fact data
            String data = Meta.get(p,Meta.FACT + "_" + fact);
            // set fact data
            Bukkit.dispatchCommand(Architect.CONSOLE,"tw facts set " + fact + " " + (data == null ? "0" : data) + " " + p.getName());
        }
    }
}
