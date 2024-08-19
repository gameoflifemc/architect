package cc.architect.managers;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Game {
    private static final PotionEffect REGENERATION = new PotionEffect(PotionEffectType.REGENERATION,PotionEffect.INFINITE_DURATION,0,false,false);
    public static void begin(Player p) {
        Game.showHud(p);
        Routines.next(p);
    }
    public static void resume(Player p) {
        // sync action points
        Actions.syncPoints(p);
    }
    public static void end(Player p) {
        // finish day
        Routines.finishDay(p);
        // hide action points
        Game.hideHud(p);
    }
    public static void hideHud(Player p) {
        
        p.removePotionEffect(PotionEffectType.REGENERATION);
    }
    public static void showHud(Player p) {
        
        p.addPotionEffect(REGENERATION);
    }
}
