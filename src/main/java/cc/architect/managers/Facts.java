package cc.architect.managers;

import cc.architect.Architect;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Facts {
    private static final AtomicInteger FACT_UID = new AtomicInteger(1);
    private static final Component INDICATOR = Component.text("Architect Fact Saver Run In Progress, ID ");
    private static String PATH;
    public static void initialize() {
        PATH = Bukkit.getPluginsFolder().getPath() + "/../logs/latest.log";
    }
    public static void restore(Player p) {
        // get all meta keys
        Set<String> keys = Meta.toUser(p).getCachedData().getMetaData().getMeta().keySet();
        // synchronize facts
        for (String fact : keys) {
            // skip non-facts
            if (!fact.startsWith(Meta.FACT)) {
                return;
            }
            // get fact id
            String id = fact.substring(5);
            // get fact data
            String data = Meta.get(p,fact);
            // set fact data
            Bukkit.dispatchCommand(Architect.CONSOLE, "tw facts set " + id + " " + data + " " + p.getName());
        }
    }
    public static void saveOne(Player p) {
        // skip if in default world
        if (p.getWorld().getName().equals("world")) {
            return;
        }
        // get uid for this request
        int uid = FACT_UID.getAndIncrement();
        // run save
        Facts.prepareSave(p,uid);
    }
    public static void saveAll() {
        // get all players
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        // skip if no players in game
        boolean noPlayers = true;
        for (Player p : players) {
            // skip if in default world
            if (p.getWorld().getName().equals("world")) {
                continue;
            }
            noPlayers = false;
        }
        if (noPlayers) {
            return;
        }
        // get uid for this request
        int uid = FACT_UID.getAndIncrement();
        // save facts for all players
        for (Player p : players) {
            // run save
            Facts.prepareSave(p,uid);
        }
    }
    private static void prepareSave(Player p, int uid) {
        // create indicator in log
        Architect.LOGGER.info(Facts.INDICATOR.append(Component.text(uid)));
        // send request command to TypeWriter
        Bukkit.dispatchCommand(Architect.CONSOLE,"tw facts " + p.getName());
        // read log
        Architect.SCHEDULER.runTaskLater(Architect.PLUGIN,() -> Facts.beginSave(p,uid),20);
    }
    private static void beginSave(Player p, int uid) {
        // read the whole log
        List<String> log = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH))) {
            // read first line
            String line = reader.readLine();
            // read all remaining lines
            while (line != null) {
                // add line to log
                log.add(line);
                // read next line
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // run through all lines
        for (String line : log) {
            // skip if not the right line
            if (!line.contains("[Server thread/INFO]: [Architect] Architect Fact Saver Run In Progress, ID " + uid)) {
                continue;
            }
            for (int i = log.indexOf(line) + 1; i < log.size(); i++) {
                // skip if not the right line
                if (!log.get(i).contains("[Server thread/INFO]: Typewriter » " + p.getName() + " has the following facts:")) {
                    continue;
                }
                // prepare facts
                List<String> factLines = new ArrayList<>();
                // run through all following lines and check for facts
                for (int j = i + 2; j < log.size(); j++) {
                    // get current line
                    String current = log.get(j);
                    // end if not a fact
                    if (!current.contains("[Server thread/INFO]:  - ")) {
                        break;
                    }
                    // add fact
                    factLines.add(current);
                }
                // save player
                Facts.savePlayer(p,factLines);
                break;
            }
            break;
        }
    }
    private static void savePlayer(Player p, List<String> lines) {
        // run through all the fact lines
        for (String line : lines) {
            // prepare objects
            int index = 36;
            StringBuilder name = new StringBuilder();
            // get whole fact name
            while (line.charAt(index) != ':') {
                name.append(line.charAt(index));
                index++;
            }
            String nameString = name.toString().toLowerCase().replace(" ","_");
            // make sure count facts are skipped
            if (nameString.contains("count")) {
                continue;
            }
            index += 2;
            StringBuilder value = new StringBuilder();
            // get whole value
            while (Character.isDigit(line.charAt(index))) {
                value.append(line.charAt(index));
                index++;
            }
            String valueString = value.toString();
            if (valueString.equals("0")) {
                continue;
            }
            // save fact
            Meta.set(p,Meta.FACT + nameString,valueString);
        }
    }
}
