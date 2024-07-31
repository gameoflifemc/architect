package cc.architect.leaderboards;

import cc.architect.Architect;
import com.maximde.hologramapi.HologramAPI;
import com.maximde.hologramapi.hologram.TextHologram;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static cc.architect.events.player.Join.activeHolograms;
import static org.bukkit.Bukkit.getServer;

public class PublicHologram {
    protected TextHologram mainHologram;
    protected Map<UUID,TextHologram> playerHolograms = new HashMap<>();
    protected Location location;

    public PublicHologram(TextHologram hologram, Location location) {
        mainHologram = hologram;
        mainHologram.spawn(location);
        mainHologram.teleport(location.clone().subtract(0,-1000,0));
        HologramAPI.getHologram().remove(mainHologram);
        this.location = location.clone();
    }
    public void setText(Component text) {
        mainHologram.setText(text);
    }
    public void init(){
        activeHolograms.add(this);
        getServer().getOnlinePlayers().forEach(this::addPlayer);
    }
    public void addPlayer(Player p) {
        TextHologram playerHologram = HologramFactory.cloneHologram(mainHologram);
        playerHologram.removeAllViewers();
        playerHologram.addViewer(p);

        playerHolograms.put(p.getUniqueId(),playerHologram);

        updateText(p);
    }
    public void updateText(Player p1){
        TextHologram hologram = playerHolograms.get(p1.getUniqueId());

        Component component = mainHologram.getTextAsComponent();

        String inText = ((TextComponent)component).content();

        String text = PlaceholderAPI.setPlaceholders(p1, "Hello %player_name%, your ping is %player_ping%");

        hologram.setText(text);

        hologram.kill();
        hologram.spawn(location);
        hologram.update();
    }

    public void updateAll(){
        getServer().getOnlinePlayers().forEach(this::updateText);
    }
}
