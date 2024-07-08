package cc.architect.objects;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

public class Messages {
    public static final Component PLUGIN_WELCOME = Component.text("Core Architect protocol running, the Game of Life is ready to begin.")
        .color(TextColor.fromHexString("#FF1313"));
    public static final Component ACTIONBAR_DIALOGUE_STANDARD = Component.text("SHIFT - Leave");
    public static final Component ACTIONBAR_DIALOGUE_RESPONSE = Component.text("SHIFT - Leave, RIGHT-CLICK - Next Response, LEFT-CLICK - Choose");
    public static final Component ACTIONBAR_DIALOGUE_CONFIRM = Component.text("SHIFT - Leave, LEFT-CLICK - Confirm");
    public static final Component INSTANCE_INITIALIZING = Component.text("Initializing simulation, please wait...");
    public static final Component INSTANCE_INITIALIZED = Component.text("Simulation instance created successfully. Ready to assimilate.");
    public static final Component INSTANCE_ASSIMILATING = Component.text("Assimilating player to simulation...");
    public static final Component INSTANCE_ASSIMILATED = Component.text("Player assimilated into simulation.");
    public static final Component INSTANCE_DECOMMISSIONING = Component.text("Decommissioning simulation...");
    public static final Component INSTANCE_DECOMMISSIONED = Component.text("Simulation decommissioned.");
    public static final Component INTERACTION_ENTITY = Component.text("Created new interaction entity.");
    public static Component SEND_INVITE_SENDER(String player){
        return Component.text("You sent invite to player "+player+".");
    };
    public static Component SEND_INVITE_RECEIVER(String player){
        return Component.text("You got invite from player "+player+". type /party accept to join.");
    };
    public static final Component SEND_INVITE_EXPIRED = Component.text("Your invite expired.");
    public static final Component SEND_INVITE_NONE = Component.text("You have no active invite.");
    public static Component SEND_INVITE_PLAYER_HAS_INVITE(String player){
        return Component.text("Player "+player+" already has invite.");
    }
    public static final Component SEND_INVITE_SELF = Component.text("You can't invite yourself.");
    public static Component SEND_INVITE_ACCEPTED(String player){
        return Component.text("Player "+player+" accepted your invite.");
    }
    public static Component SEND_INVITE_ACCEPT(String player){
        return Component.text("You accepted invite from "+player+".");
    }
    public static Component SEND_INVITE_DENIED(String player){
        return Component.text("Player "+player+" denied your invite.");
    }
    public static Component SEND_INVITE_DENY(String player){
        return Component.text("You denied invite from "+player+".");
    }
    public static Component PLAYER_NOT_ONLINE(String player){
        return Component.text("Player "+player+" is currently offline.");
    }
    public static Component SEND_INVITE_ERROR = Component.text("Error sending invite please try again later.");
}
