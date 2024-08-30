package cc.architect.objects;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Messages {
    // colors
    public static final TextColor BASE = TextColor.fromHexString("#2970ED");
    public static final TextColor GREEN = TextColor.fromHexString("#00FF00");
    public static final TextColor RED = TextColor.fromHexString("#FF0000");
    // messages
    public static final Component PLUGIN_WELCOME = Component.text("Core Architect protocol running, the Game of Life is ready to begin.").color(BASE);
    public static final Component SERVER_FULL = Component.text("Systém zaplněn.").color(RED);
    public static final Component TREASURE_FOUND = Component.text("Našel jsi poklad! Otevřeš ho pravým tlačítkem myši. Rychle, než ti ho někdo ukradne!").color(GREEN);
    public static final Component TREASURE_FOUND_AUTO_OPEN = Component.text("Našel jsi haldu ležících emeraldů, které se ti nyní přidali do inventáře.").color(GREEN);
    public static final Component STEAL = Component.text("Našel jsi poklad, ale snaží se útéct! Rychle ho chytni, než ti uteče. Stačí na něj kliknout levým tlačítkem myši.").color(RED);
    public static final Component FARMING_TILL = Component.text("You've received your hoe, go and convert as much land into farmland as you can. You have 15 seconds to do so.").color(GREEN);
    public static final Component FARMING_SEEDS = Component.text("Hopefully you have at least some farmland. But now you have to place some seeds. You have 15 seconds to do so.").color(GREEN);
    public static final Component FARMING_FERTILIZE = Component.text("After planting your seeds you should fertilize them. You have 15 seconds for that.").color(GREEN);
    public static final Component FARMING_HARVEST = Component.text("You now have your crops grown and you should harvest them. You can do that for the next 60 seconds").color(GREEN);
    public static final Component FARMING_END = Component.text("Harvesting is over. Go to the main lobby and sell your crops.").color(GREEN);
    public static final Component FARMING_TREASURE = Component.text("Your hoe got stuck under a bunch of emeralds.").color(GREEN);
    public static final Component FARMING_CANNOT_BONEMEAL = Component.text("You can't apply bone meal to that block").color(RED);
    public static final Component FARMING_CANNOT_HARVEST = Component.text("You can't harvest that block").color(RED);
    public static final Component FARMING_MUSHROOM_GUIDE = Component.text("You have to click then wait 3 seconds and then click again for successful harvest of mushrooms").color(RED);

    public static Component SEND_INVITE_SENDER(String player) {
        return Component.text("Poslal jsi pozvánku hráči " + player + ".");
    }
    public static Component SEND_INVITE_RECEIVER(String player) {
        return Component.text("Dostal jsi pozvánku od hráče " + player + ". napiš /invite accept nebo /invite deny.");
    }
    public static final Component SEND_INVITE_EXPIRED = Component.text("Tvoje pozvánka expirovala.");
    public static Component SEND_INVITE_PLAYER_HAS_INVITE(String player) {
        return Component.text("Hráč " + player + " už má aktivní pozváku, počkej než ji nevyřeší.");
    }
    public static final Component SEND_INVITE_SELF = Component.text("Nemůžeš poslat pozvánku sám sobě.");
    public static Component SEND_INVITE_ACCEPTED(String player) {
        return Component.text("Hráč " + player + " přijal tvojí pozvánku.");
    }
    public static Component SEND_INVITE_ACCEPT(String player) {
        return Component.text("Přijmul jsi pozvánku od hráče " + player + ".");
    }
    public static Component SEND_INVITE_DENIED(String player) {
        return Component.text("Hráč " + player + " zamítnul tvojí pozvánku.");
    }
    public static Component SEND_INVITE_DENY(String player) {
        return Component.text("Odmítl jsi pozvánku od hráče " + player + ".");
    }
    public static Component PLAYER_NOT_ONLINE(String player) {
        return Component.text("Hráč " + player + " aktuálně není ve hře.");
    }
    public static final Component SEND_INVITE_ERROR = Component.text("Nastala chyba při posílání pozvánky zkus to prosím později.");
    public static final Component PLAYER_ON_SAME_SERVER = Component.text("Pozvaný hráč už je na stejném serveru.");
    public static final Component NO_INVITE = Component.text("Bohužel aktuálně nemáš žádnou aktivní pozvánku.");


}
