package cc.architect.objects;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Messages {
    public static final Component PLUGIN_WELCOME = Component.text("Core Architect protocol running, the Game of Life is ready to begin.")
        .color(TextColor.fromHexString("#2970ED"));
    public static final Component SEND_INVITE_CANNOT_MAKE_PARTY = Component.text("You can't make party.");
    public static final Component PLAYER_IN_PARTY = Component.text("Player is already in party.");
    public static final Component NOT_IN_PARTY = Component.text("You are not in party.");
    public static final Component YOU_LEFT_PARTY_LEADER_LEAVE = Component.text("Leader left party, you not in party.");
    public static final Component YOU_LEFT_PARTY_MEMBERS_EMPTY = Component.text("You left party, party is empty.");
    public static final Component YOU_LEFT_PARTY = Component.text("You left party.");
    public static final Component TREASURE_FOUND = Component.text("You found a treasure.")
            .color(TextColor.fromHexString("#00FF00"));
    public static final Component STEAL = Component.text("Looks like someone's trying to steal your treasure, catch him and you'll get it back")
            .color(TextColor.fromHexString("#FF0000"));
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
    public static Component SEND_INVITE_ERROR = Component.text("Error sending invite please try again later.");
}
