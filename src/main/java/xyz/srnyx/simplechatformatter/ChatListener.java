package xyz.srnyx.simplechatformatter;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingListener;
import xyz.srnyx.annoyingapi.utility.AnnoyingUtility;


public class ChatListener implements AnnoyingListener {
    @NotNull private final SimpleChatFormatter plugin;

    public ChatListener(@NotNull SimpleChatFormatter plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public SimpleChatFormatter getPlugin() {
        return plugin;
    }

    @EventHandler
    public void onAsyncPlayerChat(@NotNull AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();

        // Set message
        String message = event.getMessage();
        if (player.hasPermission("chat.format")) message = AnnoyingUtility.color(message);
        event.setMessage(message);

        // Set format
        String format = plugin.format;
        if (plugin.papiInstalled) format = PlaceholderAPI.setPlaceholders(player, format);
        event.setFormat(AnnoyingUtility.color(format).replace("%message%", "%2$s"));
    }
}
