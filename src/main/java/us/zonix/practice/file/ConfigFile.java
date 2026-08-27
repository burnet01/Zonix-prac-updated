package us.zonix.practice.file;

import java.io.IOException;
import java.util.Iterator;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;

public class ConfigFile
{
    private File file;
    private YamlConfiguration configuration;
    
    public ConfigFile(final JavaPlugin plugin, final String name) {
        this.file = new File(plugin.getDataFolder(), name + ".yml");
        if (!this.file.getParentFile().exists()) {
            this.file.getParentFile().mkdir();
        }
        if (!this.file.exists()) {
            if (plugin.getResource(name + ".yml") != null) {
                plugin.saveResource(name + ".yml", false);
            }
            else {
                try {
                    this.file.createNewFile();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        this.configuration = new YamlConfiguration();
        try {
            this.configuration.load(this.file);
        }
        catch (Exception ex) {
            final String raw = this.readFile();
            if (raw != null && raw.indexOf('\u00a0') >= 0) {
                plugin.getLogger().warning(name + ".yml contained non-breaking spaces; auto-repairing.");
                try {
                    this.configuration.loadFromString(raw.replace('\u00a0', ' '));
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            else {
                plugin.getLogger().warning("Could not load " + name + ".yml: " + ex.getMessage());
            }
        }
    }

    private String readFile() {
        try {
            return new String(java.nio.file.Files.readAllBytes(this.file.toPath()), java.nio.charset.StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public double getDouble(final String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getDouble(path);
        }
        return 0.0;
    }
    
    public int getInt(final String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getInt(path);
        }
        return 0;
    }
    
    public boolean getBoolean(final String path) {
        return this.configuration.contains(path) && this.configuration.getBoolean(path);
    }
    
    public String getString(final String path) {
        if (this.configuration.contains(path)) {
            return ChatColor.translateAlternateColorCodes('&', this.configuration.getString(path));
        }
        return "ERROR: STRING NOT FOUND";
    }
    
    public String getString(final String path, final String callback, final boolean colorize) {
        if (!this.configuration.contains(path)) {
            return callback;
        }
        if (colorize) {
            return ChatColor.translateAlternateColorCodes('&', this.configuration.getString(path));
        }
        return this.configuration.getString(path);
    }
    
    public List<String> getReversedStringList(final String path) {
        final List<String> list = this.getStringList(path);
        if (list != null) {
            final int size = list.size();
            final List<String> toReturn = new ArrayList<String>();
            for (int i = size - 1; i >= 0; --i) {
                toReturn.add(list.get(i));
            }
            return toReturn;
        }
        return Arrays.asList("ERROR: STRING LIST NOT FOUND!");
    }
    
    public List<String> getStringList(final String path) {
        if (this.configuration.contains(path)) {
            final ArrayList<String> strings = new ArrayList<String>();
            for (final String string : this.configuration.getStringList(path)) {
                strings.add(ChatColor.translateAlternateColorCodes('&', string));
            }
            return strings;
        }
        return Arrays.asList("ERROR: STRING LIST NOT FOUND!");
    }
    
    public List<String> getStringListOrDefault(final String path, final List<String> toReturn) {
        if (this.configuration.contains(path)) {
            final ArrayList<String> strings = new ArrayList<String>();
            for (final String string : this.configuration.getStringList(path)) {
                strings.add(ChatColor.translateAlternateColorCodes('&', string));
            }
            return strings;
        }
        return toReturn;
    }
    
    public void reload() {
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }
    
    public void save() {
        try {
            this.configuration.save(this.file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public File getFile() {
        return this.file;
    }
    
    public YamlConfiguration getConfiguration() {
        return this.configuration;
    }
}
