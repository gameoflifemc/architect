package cc.architect.managers;

import cc.architect.Architect;
import org.bukkit.Bukkit;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.File;
import java.util.HashMap;

public class Configurations {
    public static final HashMap<String, TomlParseResult> CONFIGS = new HashMap<>();
    public static void load() {
        // get base path
        String basePath = Bukkit.getPluginsFolder().getPath() + File.separator + Architect.PLUGIN.getName() + File.separator;
        // get folder
        File folder = new File(basePath);
        // get all files
        String[] files = folder.list();
        if (files == null) {
            return;
        }
        // load all configs
        for (String file : files) {
            // load config
            TomlParseResult config = Toml.parse(basePath + file);
            // put into map
            CONFIGS.put(file.substring(0, file.length() - 5), config);
        }
    }
}
