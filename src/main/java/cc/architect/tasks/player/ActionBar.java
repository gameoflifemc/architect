package cc.architect.tasks.player;

import cc.architect.objects.Fonts;
import cc.architect.objects.HashMaps;
import net.kyori.adventure.text.Component;

public class ActionBar implements Runnable {
    private static final Component message = Component.text("@").font(Fonts.LOADING);
    @Override
    public void run() {
        HashMaps.ACTION_BAR.forEach(p -> p.sendActionBar(message));
    }
}
