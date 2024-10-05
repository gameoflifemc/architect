package cc.architect.managers;

import cc.architect.Architect;
import cc.architect.objects.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Ilness {

    /**
     * Add ilness to player
     * @param player player
     * @param amount amount (max 1000)
     */
    public static void addIlness(Player player, int amount) {
        if(Meta.get(player, Meta.ILNESS)==null) {
            Meta.set(player, Meta.ILNESS, amount+"");
            return;
        }

        int current = Integer.parseInt(Meta.get(player, Meta.ILNESS));
        Meta.set(player, Meta.ILNESS, Math.min(current+amount,1000)+"");

        if (current+amount>=1000) {
            startIlness(player);
        }else if (current+amount>500 && Architect.RANDOM.nextInt(current+amount, 1001)==1000) {
            startIlness(player);
        }
    }

    public static void setup(Player player) {
        Meta.set(player, Meta.ILNESS, "0");
        Meta.set(player, Meta.IS_ILl, "0");
        player.removePotionEffect(PotionEffectType.SLOWNESS);
    }

    public static void startIlness(Player player) {
        player.sendMessage(Component.text("---------------------------------------------").color(Colors.RED));
        player.sendMessage(Component.text("Cítíš se nemocný, dojdi si radši za doktorem.").color(Colors.RED));
        player.sendMessage(Component.text("---------------------------------------------").color(Colors.RED));
        Meta.set(player, Meta.IS_ILl, "1");
        addPotion(player);
    }

    public static void cure(Player p) {
        if(Meta.get(p, Meta.IS_ILl)==null) {
            Meta.set(p, Meta.IS_ILl, "0");
            sendNotIllMsg(p);
            return;
        }
        if(Meta.get(p, Meta.ILNESS)==null) {
            sendNotIllMsg(p);
            return;
        }
        if(Integer.parseInt(Meta.get(p, Meta.ILNESS))<500) {
            sendNotIllMsg(p);
            return;
        }
        if(Integer.parseInt(Meta.get(p, Meta.ILNESS))<700) {
            p.sendMessage(Component.text("Vypadá to, že jsi opravdu byl trochu nemocný, doktor si proto vzal jen 30 emeraldů a vyléčil tě.").color(Colors.GREEN));
            p.getInventory().removeItemAnySlot(new ItemStack(Material.EMERALD,30));
            Meta.set(p, Meta.ILNESS, "0");
            return;
        }
        p.sendMessage(Component.text("Byl jsi vyléčen, doktor si proto vzal 60 emeraldů.").color(Colors.GREEN));
        p.getInventory().removeItemAnySlot(new ItemStack(Material.EMERALD,60));

        setup(p);
    }

    public static void sendNotIllMsg(Player p) {
        p.sendMessage(Component.text("Vypadá to, že jsi zdravý.").color(Colors.GREEN));
    }

    public static void addIfPotion(Player player) {
        if(Meta.get(player, Meta.IS_ILl)==null) {
            Meta.set(player, Meta.IS_ILl, "0");
            return;
        }
        if (Meta.get(player, Meta.IS_ILl).equals("1")) {
            player.sendMessage(Component.text("---------------------------------------------").color(Colors.RED));
            player.sendMessage(Component.text("Cítíš se nemocný, dojdi si radši za doktorem.").color(Colors.RED));
            player.sendMessage(Component.text("---------------------------------------------").color(Colors.RED));
            addPotion(player);
        }
    }

    public static void addPotion(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 1));
    }
}
