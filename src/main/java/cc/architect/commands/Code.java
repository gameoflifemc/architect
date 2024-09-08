package cc.architect.commands;

import cc.architect.managers.Meta;
import cc.architect.objects.Colors;
import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class Code {
    private static final Component ICON = Component.text("\uE004 ").color(TextColor.fromHexString("#FFFFFF"));
    private static final Component PREFIX = Component.text("\"").color(Colors.GREEN);
    private static final Component SUFFIX = Component.text("\" je tvůj registrační kód. Stiskni \"T\" a klikni zde pro zkopírování kódu.").color(Colors.GREEN);
    public static void register(LifecycleEventManager<Plugin> manager) {
        // create and register command
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            // register command
            event.registrar().register(Commands.literal("code").executes(ctx -> {
                Player p = Bukkit.getPlayerExact(ctx.getSource().getSender().getName());
                if (p == null) {
                    return Command.SINGLE_SUCCESS;
                }
                String code = Meta.check(p,Meta.CODE) ? Meta.get(p,Meta.CODE) : Code.generate(p);
                p.sendMessage(ICON.append(PREFIX).append(Component.text(code).color(Colors.GREEN).decorate(TextDecoration.UNDERLINED)).append(SUFFIX).clickEvent(ClickEvent.copyToClipboard(code)));
                return Command.SINGLE_SUCCESS;
            })
            .build()
            );
        });
    }
    private static String generate(Player p) {
        String code = "gol_" + RandomStringUtils.randomAlphanumeric(16);
        Meta.set(p,Meta.CODE,code);
        return code;
    }
}
