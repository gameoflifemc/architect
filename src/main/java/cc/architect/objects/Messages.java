package cc.architect.objects;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Messages {
    public static final TextColor GREEN = TextColor.fromHexString("#00FF00");
    public static final TextColor RED = TextColor.fromHexString("#FF0000");
    public static final Component PLUGIN_WELCOME = Component.text("Core Architect protocol running, the Game of Life is ready to begin.")
        .color(TextColor.fromHexString("#2970ED"));
    public static final Component SEND_INVITE_CANNOT_MAKE_PARTY = Component.text("You can't make party.");
    public static final Component PLAYER_IN_PARTY = Component.text("Player is already in party.");
    public static final Component NOT_IN_PARTY = Component.text("You are not in party.");
    public static final Component YOU_LEFT_PARTY_LEADER_LEAVE = Component.text("Leader left party, you not in party.");
    public static final Component YOU_LEFT_PARTY_MEMBERS_EMPTY = Component.text("You left party, party is empty.");
    public static final Component YOU_LEFT_PARTY = Component.text("You left party.");
    public static final Component TREASURE_FOUND = Component.text("Našel jsi poklad! Otevřeš ho pravým tlačítkem myši. Rychle, než ti ho někdo ukradne!")
            .color(GREEN);
    public static final Component TREASURE_FOUND_AUTO_OPEN = Component.text("Našel jsi haldu ležících emeraldů, které se ti nyní přidali do inventáře.")
        .color(GREEN);
    public static final Component STEAL = Component.text("Našel jsi poklad, ale snaží se útéct! Rychle ho chytni, než ti uteče. Stačí na něj kliknout levým tlačítkem myši.")
            .color(TextColor.fromHexString("#FF0000"));
    public static final Component BLOCK_MINED = Component.text("You can't mine this block, because you've already mined it.").color(TextColor.fromHexString("#FF0000"));
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
        return Component.text("You sent invite to player " + player + ".");
    }
    public static Component SEND_INVITE_RECEIVER(String player) {
        return Component.text("You got invite from player " + player + ". type /party accept to join.");
    }
    public static final Component SEND_INVITE_EXPIRED = Component.text("Your invite expired.");
    public static final Component SEND_INVITE_NONE = Component.text("You have no active invite.");
    public static Component SEND_INVITE_PLAYER_HAS_INVITE(String player) {
        return Component.text("Player " + player + " already has invite.");
    }
    public static final Component SEND_INVITE_SELF = Component.text("You can't invite yourself.");
    public static Component SEND_INVITE_ACCEPTED(String player) {
        return Component.text("Player " + player + " accepted your invite.");
    }
    public static Component SEND_INVITE_ACCEPT(String player) {
        return Component.text("You accepted invite from " + player + ".");
    }
    public static Component SEND_INVITE_DENIED(String player) {
        return Component.text("Player " + player + " denied your invite.");
    }
    public static Component SEND_INVITE_DENY(String player) {
        return Component.text("You denied invite from " + player + ".");
    }
    public static Component PLAYER_NOT_ONLINE(String player) {
        return Component.text("Player " + player + " is currently offline.");
    }
    public static final Component SEND_INVITE_ERROR = Component.text("Error sending invite please try again later.");
}
