package xyz.srnyx.simplechatformatter.listeners;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingListener;
import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.utility.BukkitUtility;

import xyz.srnyx.simplechatformatter.SimpleChatFormatter;

import java.util.UnknownFormatConversionException;
import java.util.logging.Level;


public class ChatListener implements AnnoyingListener {
    @NotNull private final SimpleChatFormatter plugin;

    public ChatListener(@NotNull SimpleChatFormatter plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public SimpleChatFormatter getAnnoyingPlugin() {
        return plugin;
    }

    @EventHandler
    public void onAsyncPlayerChat(@NotNull AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();

        // Format message
        if (player.hasPermission("chat.format")) event.setMessage(BukkitUtility.color(event.getMessage()));

        // Set format
        String format = plugin.format;
        if (plugin.papiInstalled) format = PlaceholderAPI.setPlaceholders(player, format);
        format = BukkitUtility.color(format);
        try {
            event.setFormat(format
                    .replace("%player%", "%1$s")
                    .replace("%message%", "%2$s"));
        } catch (final UnknownFormatConversionException e) {
            AnnoyingPlugin.log(Level.WARNING, "Formatting error! Look for any unparsed placeholders (except %player% and %message%): " + format);
        }
    }
}
