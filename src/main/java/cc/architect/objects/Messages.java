package cc.architect.objects;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Messages {
    public static final Component WELCOME = Component.text("Core Architect protocol running, the Game of Life is ready to begin.")
        .color(TextColor.fromHexString("#FF1313"));
    public static final Component STANDARD = Component.text("SHIFT - Leave");
    public static final Component RESPONSE = Component.text("SHIFT - Leave, RIGHT-CLICK - Next Response, LEFT-CLICK - Choose");
    public static final Component CONFIRM = Component.text("SHIFT - Leave, LEFT-CLICK - Confirm");
}
