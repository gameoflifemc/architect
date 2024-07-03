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
    /**
     * A list of 4 responses for a dialogue interaction.
     * @param uid unique identifier of the dialogue.
     */
    public ResponseList(String uid) {
        // get config
        FileConfiguration config = Architect.PLUGIN.getConfig();
        // get responses
        for (int i = 1; i <= 4; i++) {
            List<String> response = config.getStringList("dialogues." + uid + ".response" + i);
            responses.add(new Response(response.get(0), response.get(1)));
        }
    }
    /**
     * Get the components for a response in the response list. Each response has two lines of text and an empty line for offset purposes.
     * @param i The index of the response to get components for.
     * @return An array of components for the response.
     */
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
        components[1] = Component.text("$" + background + "$" + firstLine).font(Fonts.DIALOGUE);
        // get aligner for the second line
        String aligner = chosen == i && confirmed ? "#" : "";
        // set second line
        components[0] = Component.text(aligner + "@$ " + secondLine).font(Fonts.DIALOGUE);
        // return finished components
        return components;
    }
    /**
     * Choose the next response in the response list.
     * @param p The player that this response list belongs to.
     */
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
        p.sendActionBar(Messages.ACTIONBAR_DIALOGUE_RESPONSE);
    }
    /**
     * Before a response is sent, it must be confirmed. This method will either confirm the current response or, if it is already confirmed, send it.
     * @param p The player that this response list belongs to.
     */
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
            p.sendActionBar(Messages.ACTIONBAR_DIALOGUE_STANDARD);
        } else {
            // confirm response
            confirmed = true;
            // update scoreboard
            Scoreboards.show(p);
            // send controls
            p.sendActionBar(Messages.ACTIONBAR_DIALOGUE_CONFIRM);
        }
    }
    /**
     * Check if the current response is confirmed. See {@link #confirmOrSend(Player)} for information about response confirmation.
     * @return True if the response is confirmed, false otherwise.
     */
    public boolean isConfirmed() {
        return confirmed;
    }
}
