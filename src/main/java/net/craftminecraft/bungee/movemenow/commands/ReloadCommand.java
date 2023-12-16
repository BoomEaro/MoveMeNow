package net.craftminecraft.bungee.movemenow.commands;

import net.craftminecraft.bungee.movemenow.MoveMeNow;
import net.craftminecraft.bungee.movemenow.managers.ConfigManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCommand extends Command {

    private final MoveMeNow moveMeNow;
    private final ConfigManager configManager;

    public ReloadCommand(MoveMeNow moveMeNow, ConfigManager configManager) {
        super("mmn", "movemenow.admin");
        this.moveMeNow = moveMeNow;
        this.configManager = configManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(TextComponent.fromLegacyText("Please use /mmn reload."));
            return;
        }

        if (args[0].equals("reload")) {
            this.configManager.load(this.moveMeNow);
            sender.sendMessage(TextComponent.fromLegacyText("Configuration successfully reloaded!"));
        }
    }
}