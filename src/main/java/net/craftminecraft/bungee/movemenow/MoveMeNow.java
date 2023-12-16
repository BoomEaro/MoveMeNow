package net.craftminecraft.bungee.movemenow;

import net.craftminecraft.bungee.movemenow.commands.ReloadCommand;
import net.craftminecraft.bungee.movemenow.listeners.PlayerListener;
import net.craftminecraft.bungee.movemenow.managers.ConfigManager;
import net.md_5.bungee.api.plugin.Plugin;

public class MoveMeNow extends Plugin {

    private static MoveMeNow instance;

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;

        this.configManager = new ConfigManager();
        this.configManager.load(this);

        this.getProxy().getPluginManager().registerListener(this, new PlayerListener(this.configManager));
        this.getProxy().getPluginManager().registerCommand(this, new ReloadCommand(this, this.configManager));

        this.getLogger().info("Plugin successfully enabled!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin successfully disabled!");
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public static MoveMeNow getInstance() {
        return instance;
    }
}