package cc.architect.objects;

import cc.architect.Architect;
import cc.architect.Utilities;
import cc.architect.managers.Scoreboards;
import cc.architect.records.Response;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ResponseList {
    private final List<Response> responses = new ArrayList<>(4);
    private int chosen = 3;
    private boolean cooldown = false;
    private boolean confirmed = false;
    public ResponseList(String id) {
        // get config
        FileConfiguration config = Architect.PLUGIN.getConfig();
        // get responses
        for (int i = 1; i <= 4; i++) {
            List<String> response = config.getStringList("dialogues." + id + ".response" + i);
            responses.add(new Response(response.get(0), response.get(1)));
        }
    }
    public Component[] getComponents(int i) {
        // get response
        Response response = responses.get(i);
        // create components
        Component[] components = new Component[3];
        // the third component is always empty
        components[2] = Component.empty();
        // get formatted response lines
        String firstLine = Utilities.addNegativeSpaces(response.firstLine());
        String secondLine = Utilities.addNegativeSpaces(response.secondLine());
        // get background character
        String background = chosen == i ? confirmed ? "#&#" : "^" : "%";
        // set first line
        components[1] = Component.text("$" + background + "$" + firstLine).font(Fonts.DIALOGUE_FONT);
        // get aligner for the second line
        String aligner = chosen == i && confirmed ? "#" : "";
        // set second line
        components[0] = Component.text(aligner + "@$ " + secondLine).font(Fonts.DIALOGUE_FONT);
        // return finished components
        return components;
    }
    public void chooseNext(Player p) {
        // check for cooldown
        if (cooldown) {
            return;
        }
        // activate cooldown
        cooldown = true;
        // schedule cooldown reset
        Bukkit.getScheduler().runTaskLater(Architect.PLUGIN,() -> cooldown = false,1);
        // reset confirm indicator
        confirmed = false;
        // get next response
        int next = chosen - 1;
        // increment chosen
        chosen = next < 0 ? 3 : next;
        // update scoreboard
        Scoreboards.show(p);
        // send controls
        p.sendActionBar(Messages.RESPONSE);
    }
    public void confirmOrSend(Player p) {
        // check if response is confirmed
        if (confirmed) {
            // get response
            Response response = responses.get(chosen);
            // remove objective
            Scoreboards.remove(p);
            // send response
            Bukkit.broadcast(Component.text(response.firstLine() + " " + response.secondLine()));
            // send controls
            p.sendActionBar(Messages.STANDARD);
        } else {
            // confirm response
            confirmed = true;
            // update scoreboard
            Scoreboards.show(p);
            // send controls
            p.sendActionBar(Messages.CONFIRM);
        }
    }
    public boolean isConfirmed() {
        return confirmed;
    }
}
