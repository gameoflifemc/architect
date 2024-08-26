package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.bonuses.DiamondBonus;
import cc.architect.objects.Compass;
import cc.architect.objects.HashMaps;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Game {
    private static final PotionEffect REGENERATION = new PotionEffect(PotionEffectType.REGENERATION,PotionEffect.INFINITE_DURATION,0,false,false);
    public static void begin(Player p) {
        Meta.set(p,Meta.DAYS,"0");
        Game.enter(p);
        Meta.set(p,Meta.ROUTINE,"0");
        Routines.switchToNext(p);
    }
    public static void resume(Player p) {
        Game.enter(p);
        Movers.showTransition(p);
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> {
            // teleport to last location
            String[] data = Meta.get(p, Meta.LAST_LOCATION).split(",");
            World world = Bukkit.getWorld(data[0]);
            if (world == null) {
                return;
            }
            p.teleport(new Location(world, Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]), Float.parseFloat(data[4]), Float.parseFloat(data[5])));
            switch (Meta.get(p,Meta.ROUTINE)) {
                case "0":
                    Time.interpolate(p,Long.parseLong(Meta.get(p,Meta.LAST_TIME)),9000);
                    break;
                case "1":
                    Time.interpolate(p,Long.parseLong(Meta.get(p,Meta.LAST_TIME)),18000);
                    break;
            }
        },100);
    }
    public static void end(Player p) {
    }
    private static void enter(Player p) {
        Game.exitLobby(p);
        // create compass
        if (!HashMaps.COMPASSES.containsKey(p)) {
            // create compass
            HashMaps.COMPASSES.put(p, new Compass(p));
        }
        // initialize bonus
        DiamondBonus.initPlayer(p);
    }
    public static void exit(Player p) {
        // delete compass
        HashMaps.COMPASSES.remove(p);
        // remove regeneration
        p.removePotionEffect(PotionEffectType.REGENERATION);
    }
    public static void enterLobby(Player p) {
        // give player spyglass
        PlayerInventory inv = p.getInventory();
        HashMaps.REPLACEMENT_ITEM.put(p,inv.getItemInMainHand());
        inv.setItemInMainHand(Items.SPYGLASS);
        // add player to action bar
        HashMaps.ACTION_BAR.add(p);
    }
    public static void exitLobby(Player p) {
        // remove spyglass
        Items.removeSpyglass(p);
        // remove player from action bar
        HashMaps.ACTION_BAR.remove(p);
        p.sendActionBar(Component.empty());
        // give regeneration
        p.addPotionEffect(REGENERATION);
    }
}
