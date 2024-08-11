package cc.architect.tasks.player;

import cc.architect.managers.Meta;
import cc.architect.objects.Fonts;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

public class ActionPoints implements Runnable {
    private static final String READY = "1.";
    private static final String EMPTY = "0.";
    private static final String END = "=";
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            int points = Integer.parseInt(Meta.getValue(p,"points"));
            String text = READY.repeat(points) + EMPTY.repeat(10 - points) + END;
            p.sendActionBar(Component.text(text).font(Fonts.ACTION));
        });
    }
}
