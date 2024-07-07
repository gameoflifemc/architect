package cc.architect.managers;

import org.bukkit.Bukkit;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.File;
import java.util.HashMap;

public class Residents {
    public static final HashMap<String, TomlParseResult> CONFIGS = new HashMap<>();
    public static void loadConfigs() {
        // get base path
        String basePath = Bukkit.getPluginsFolder().getPath() + File.separator + ".residents" + File.separator;
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
