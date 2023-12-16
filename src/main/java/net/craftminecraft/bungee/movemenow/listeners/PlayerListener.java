package net.craftminecraft.bungee.movemenow.listeners;

import net.craftminecraft.bungee.movemenow.managers.ConfigManager;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class PlayerListener implements Listener {

    private final ConfigManager configManager;

    public PlayerListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onServerKickEvent(ServerKickEvent ev) {
        ServerInfo kickedFrom;

        if (ev.getPlayer().getServer() != null) {
            kickedFrom = ev.getPlayer().getServer().getInfo();
        } else if (ProxyServer.getInstance().getReconnectHandler() != null) {// If first server and reconnect handler
            kickedFrom = ProxyServer.getInstance().getReconnectHandler().getServer(ev.getPlayer());
        } else { // If first server and no reconnect handler
            kickedFrom = AbstractReconnectHandler.getForcedHost(ev.getPlayer().getPendingConnection());
            if (kickedFrom == null) // Can still be null if vhost is null... 
            {
                kickedFrom = ProxyServer.getInstance().getServerInfo(ev.getPlayer().getPendingConnection().getListener().getDefaultServer());
            }
        }

        ServerInfo kickTo = findServerWithMinimalPlayers(this.configManager.getServers());
        if (kickTo == null) {
            return;
        }

        // Avoid the loop
        if (kickedFrom != null && kickedFrom.equals(kickTo)) {
            return;
        }

        String reason = BaseComponent.toLegacyText(ev.getKickReasonComponent());
        String[] moveMsg = this.configManager.getMessage().replace("%kickmsg%", reason).split("\n");

        List<String> reasons = this.configManager.getReasons();
        if (this.configManager.isWhitelist()) {
            for (String currentReason : reasons) {
                if (reason.contains(currentReason)) {
                    ev.setCancelled(true);
                    ev.setCancelServer(kickTo);
                    if (!(moveMsg.length == 1 && moveMsg[0].isEmpty())) {
                        for (String line : moveMsg) {
                            ev.getPlayer().sendMessage(TextComponent.fromLegacyText(line));
                        }
                    }
                    break; // no need to keep this up !
                }
            }
        } else {
            for (String currentReason : reasons) {
                if (reason.contains(currentReason)) {
                    return;
                }
            }
            ev.setCancelled(true);
            ev.setCancelServer(kickTo);
            if (!(moveMsg.length == 1 && moveMsg[0].isEmpty())) {
                for (String line : moveMsg) {
                    ev.getPlayer().sendMessage(TextComponent.fromLegacyText(line));
                }
            }
        }
    }

    private static ServerInfo findServerWithMinimalPlayers(List<String> servers) {
        int count = Integer.MAX_VALUE;
        ServerInfo find = null;
        for (String serverName : servers) {
            ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverName);
            if (serverInfo == null) {
                continue;
            }

            int currentPlayers = serverInfo.getPlayers().size();
            if (currentPlayers < count) {
                count = currentPlayers;
                find = serverInfo;
            }
        }

        return find;
    }
}
