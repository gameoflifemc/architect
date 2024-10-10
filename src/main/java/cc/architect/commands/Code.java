package cc.architect.commands;

import cc.architect.managers.Meta;
import cc.architect.objects.Colors;
import cc.architect.objects.Icons;
import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Code {
    private static final Component PREFIX = Component.text("\"");
    private static final Component SUFFIX = Component.text("\" je tvůj registrační kód.");
    private static final Component NEXT_LINE = Component.text("Stiskni \"T\" a klikni zde pro zkopírování kódu.");
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
                p.sendMessage(Icons.SUCCESS.append((PREFIX).append(Component.text(code)).append(SUFFIX).appendNewline().append(NEXT_LINE).color(Colors.GREEN)).clickEvent(ClickEvent.copyToClipboard(code)));
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
