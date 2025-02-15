package cc.architect.objects;

import net.kyori.adventure.text.Component;

public class Messages {
    public static final Component PLUGIN_WELCOME = Component.text("Core Architect protocol running, the Game of Life is ready to begin.").color(Colors.BASE);
    public static final Component SERVER_FULL = Component.text("Systém zaplněn.").color(Colors.RED);
    public static final Component TREASURE_FOUND = Component.text("Našel jsi poklad! Otevřeš ho pravým tlačítkem myši. Rychle, než ti ho někdo ukradne!").color(Colors.GREEN);
    public static final Component TREASURE_FOUND_AUTO_OPEN = Component.text("Našel jsi haldu ležících emeraldů, které se ti nyní přidali do inventáře.").color(Colors.GREEN);
    public static final Component STEAL = Component.text("Našel jsi poklad, ale snaží se útéct! Rychle ho chytni, než ti uteče. Stačí na něj kliknout levým tlačítkem myši.").color(Colors.GREEN);
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
