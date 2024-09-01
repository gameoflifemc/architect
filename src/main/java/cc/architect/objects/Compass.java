package cc.architect.objects;

import lombok.Getter;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@Getter
public class Compass {
    private final BossBar directions = BossBar.bossBar(
        Component.empty(),
        BossBar.MAX_PROGRESS,
        BossBar.Color.WHITE,
        BossBar.Overlay.PROGRESS
    );
    private final BossBar locations = BossBar.bossBar(
        Component.empty(),
        BossBar.MAX_PROGRESS,
        BossBar.Color.WHITE,
        BossBar.Overlay.PROGRESS
    );
    private final BossBar background = BossBar.bossBar(
        Component.text("@").font(Fonts.COMPASS),
        BossBar.MAX_PROGRESS,
        BossBar.Color.WHITE,
        BossBar.Overlay.PROGRESS
    );
    public Compass(Player p) {
        // the order of bossbars is important
        background.addViewer(p);
        directions.addViewer(p);
        locations.addViewer(p);
    }
}
