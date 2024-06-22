package cc.architect.objects;

import cc.architect.records.Response;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

import static cc.architect.Architect.plugin;
import static cc.architect.Utilities.addNegativeSpaces;

public class ResponseList {
    private static final Key DIALOGUE_FONT = Key.key("minecraft:dialogue");
    private final List<Response> responses = new ArrayList<>();
    private int chosenCurrent = 0;
    private int chosenPrevious = 0;
    public ResponseList(String id) {
        // get config
        FileConfiguration config = plugin.getConfig();
        // get responses
        for (int i = 1; i <= 4; i++) {
            List<String> response = config.getStringList("dialogues." + id + ".response" + i);
            responses.add(new Response(response.get(0), response.get(1)));
        }
    }
    public Component[] getComponents(int i) {
        // get response
        Response response = responses.get(i);
        // set base components
        Component[] components = new Component[3];
        components[2] = Component.empty();
        // get formatted response lines
        String firstLine = addNegativeSpaces(response.firstLine());
        String secondLine = addNegativeSpaces(response.secondLine());
        // set response components
        components[1] = Component.text("&$&" + firstLine).font(DIALOGUE_FONT);
        components[0] = Component.text("&# " + secondLine).font(DIALOGUE_FONT);
        // return components
        return components;
    }
    public int size() {
        return responses.size();
    }
    public int getChosenCurrent() {
        return chosenCurrent;
    }
    public int getChosenPrevious() {
        return chosenPrevious;
    }
    public void chooseNext() {
        // save previous choice
        chosenPrevious = chosenCurrent;
        // increment choice
        int next = chosenCurrent + 1;
        if (next > 3) {
            next = 0;
        }
        chosenCurrent = next;
    }
    public void choosePrevious() {
        // save previous choice
        chosenPrevious = chosenCurrent;
        // decrement choice
        int last = chosenCurrent - 1;
        if (last < 0) {
            last = 3;
        }
        chosenCurrent = last;
    }
}
