package cc.architect.managers;

import cc.architect.Architect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Facts {
    private static String FACT_PATH;
    public static void initialize() {
        FACT_PATH = Bukkit.getPluginsFolder().getPath() + "/Typewriter/facts.json";
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
        if (p.getWorld().getName().equals("world")) {
            return;
        }
        Facts.save(p,Facts.get());
    }
    public static void saveAll() {
        String line = Facts.get();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equals("world")) {
                continue;
            }
            Facts.save(p,line);
        }
    }
    private static String get() {
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(FACT_PATH))) {
            line = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return line;
    }
    private static void save(Player p,String line) {
        // get indexes of uuid
        List<Integer> uuids = new ArrayList<>();
        String matcher = p.getUniqueId().toString();
        int index = line.indexOf(matcher);
        while (index >= 0) {
            uuids.add(index);
            index = line.indexOf(matcher,index + 1);
        }
        // run through all uuid indexes
        for (int uuid : uuids) {
            // get fact index
            int fact = uuid - 19;
            // get value index
            int value = uuid + 47;
            // prepare string builder
            StringBuilder builder = new StringBuilder();
            // get whole value
            while (Character.isDigit(line.charAt(value))) {
                builder.append(line.charAt(value));
                value++;
            }
            // save fact
            Meta.set(p,Meta.FACT + line.substring(fact,fact + 15),builder.toString());
        }
    }
}
