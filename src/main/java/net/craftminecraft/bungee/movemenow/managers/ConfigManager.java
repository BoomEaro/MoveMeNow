package net.craftminecraft.bungee.movemenow.managers;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;

public class ConfigManager {

    private String message;
    private List<String> servers;
    private boolean whitelist;
    private List<String> reasons;

    public void load(Plugin plugin) {
        try {
           Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(loadResource(plugin, "config.yml"));

           this.message = ChatColor.translateAlternateColorCodes('&', configuration.getString("message"));
           this.servers = configuration.getStringList("servers");
           this.whitelist = configuration.getBoolean("whitelist");
           this.reasons = configuration.getStringList("reasons");

        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Exception while reading config", e);
        }
    }

    public String getMessage() {
        return this.message;
    }

    public List<String> getServers() {
        return this.servers;
    }

    public boolean isWhitelist() {
        return this.whitelist;
    }

    public List<String> getReasons() {
        return this.reasons;
    }

    private File loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists()) {
            folder.mkdir();
        }
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = plugin.getResourceAsStream(resource);
                     OutputStream out = Files.newOutputStream(resourceFile.toPath())) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Exception while writing default config", e);
        }
        return resourceFile;
    }
}
