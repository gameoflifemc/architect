package cc.architect.managers;

import cc.architect.objects.Messages;
import org.bukkit.entity.Player;

public class Game {
    public static void begin(Player p) {
        Actions.showPoints(p);
        Routines.next(p);
    }
    public static void resume(Player p) {
        // welcome back message
        p.sendMessage(Messages.WELCOME_BACK);
        // sync action points
        Actions.syncPoints(p);
    }
    public static void end(Player p) {
        // finish day
        Routines.finishDay(p);
        // hide action points
        Actions.hidePoints(p);
    }
}
