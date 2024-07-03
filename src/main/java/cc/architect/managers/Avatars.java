package cc.architect.managers;

import cc.architect.objects.Fonts;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class Avatars {
    /**
     * Creates a single Component for displaying the player's head
     */
    public static Component create(Player p) throws IOException {
        // create url
        URL url = URI.create("https://minotar.net/avatar/" + p.getUniqueId() + "/8").toURL();
        // open connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // set user agent
        connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        // get image of avatar
        BufferedImage image = ImageIO.read(connection.getInputStream());
        // prepare builder
        ComponentBuilder<TextComponent, TextComponent.Builder> builder = Component.text();
        // iterate through y coordinates
        for (int y = 0; y < 8; y++) {
            // pick the correct char
            Component character = CHARS.get(y);
            // iterate through x coordinates
            for (int x = 0; x < 8; x++) {
                // get the pixel color
                TextColor textColor = TextColor.color(image.getRGB(x,y));
                // add color to the character
                Component pixel = character.color(textColor);
                // add pixel to the builder
                builder.append(pixel).append(MINUS);
            }
            // add a new line
            builder.append(PLUS);
        }
        // return the built component
        return builder.build().font(Fonts.AVATAR);
    }
    private static final List<Component> CHARS = List.of(
        Component.text('!'),
        Component.text('@'),
        Component.text('#'),
        Component.text('$'),
        Component.text('%'),
        Component.text('^'),
        Component.text('&'),
        Component.text('*')
    );
    private static final Component PLUS = Component.text('+');
    private static final Component MINUS = Component.text('-');
}
