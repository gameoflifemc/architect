package cc.architect.commands;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class Discord {
    private static final Component ICON = Component.text("\uE001 ").color(TextColor.fromHexString("#FFFFFF"));
    private static final Component DISCORD = Component.text("Připoj se do naší komunity na Discordu! Zde můžeš najít informace a novinky nebo nám poslat své nápady a připomínky. Stačí kliknout na tento odkaz: ")
        .append(Component.text("https://discord.gol.nvias.org").decorate(TextDecoration.UNDERLINED).clickEvent(ClickEvent.openUrl("https://discord.gol.nvias.org")))
        .color(TextColor.fromHexString("#7289DA"));
    public static void register(LifecycleEventManager<Plugin> manager) {
        // create and register command
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            // register command
            event.registrar().register(Commands.literal("discord").executes(ctx -> {
                Player p = Bukkit.getPlayerExact(ctx.getSource().getSender().getName());
                if (p == null) {
                    return Command.SINGLE_SUCCESS;
                }
                p.sendMessage(ICON.append(DISCORD));
                return Command.SINGLE_SUCCESS;
            })
                .build()
            );
        });
    }
}
