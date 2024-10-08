package cc.architect.commands;

import cc.architect.channels.ProxyLogger;
import cc.architect.managers.Meta;
import cc.architect.objects.Colors;
import cc.architect.objects.Icons;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ScoreGiver {
    public static void give(Player p, int amount) {
        Location loc = p.getLocation().clone();
        loc.setX(Math.round(loc.getX()));
        loc.setY(Math.round(loc.getY()));
        loc.setZ(Math.round(loc.getZ()));
        loc.setPitch(Math.round(loc.getPitch()));
        loc.setYaw(Math.round(loc.getYaw()));

        Meta.add(p,Meta.SCORE_TOTAL,amount);
        p.sendMessage(Icons.SUCCESS.append(Component.text("Získáno " + amount + " skóre. Celkem " + Meta.get(p,Meta.SCORE_TOTAL) + " skóre.").color(Colors.GREEN)));
        ProxyLogger.logInfoProxy(p.getName() + " location: "+loc.toString()+" amount: "+amount);
    }
}
