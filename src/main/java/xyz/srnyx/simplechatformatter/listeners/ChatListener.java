package xyz.srnyx.simplechatformatter.listeners;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingListener;
import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;
import xyz.srnyx.annoyingapi.utility.BukkitUtility;

import xyz.srnyx.simplechatformatter.SimpleChatFormatter;
import xyz.srnyx.simplechatformatter.SimpleSimilarity;

import java.util.*;
import java.util.logging.Level;


public class ChatListener implements AnnoyingListener {
    @NotNull private final SimpleChatFormatter plugin;
    @NotNull private final Map<UUID, List<TimedMessage>> messages = new HashMap<>();

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

        // Spam
        if ((plugin.config.spamSpeedEnabled || plugin.config.spamSimilarityEnabled) && !player.hasPermission("chat.spam.bypass")) {
            final String message = event.getMessage();
            final String messageLower = message.toLowerCase();
            final List<TimedMessage> playerMessages = this.messages.computeIfAbsent(player.getUniqueId(), uuid -> new ArrayList<>());
            if (!playerMessages.isEmpty()) {
                // Similarity
                if (plugin.config.spamSimilarityEnabled) {
                    final double similarity = SimpleSimilarity.apply(playerMessages.get(playerMessages.size() - 1).message, messageLower);
                    if (similarity >= plugin.config.spamSimilarityPercent) {
                        event.setCancelled(true);
                        new AnnoyingMessage(plugin, "spam.similarity")
                                .replace("%similarity%", (int) Math.abs(similarity * 100))
                                .replace("%message%", message)
                                .send(player);
                        return;
                    }
                }

                // Speed
                if (plugin.config.spamSpeedEnabled) {
                    playerMessages.removeIf(msg -> msg.time < System.currentTimeMillis() - plugin.config.spamSpeedTime);
                    if (playerMessages.size() + 1 > plugin.config.spamSpeedMessages) {
                        event.setCancelled(true);
                        new AnnoyingMessage(plugin, "spam.speed")
                                .replace("%message%", message)
                                .send(player);
                        return;
                    }
                } else {
                    playerMessages.clear();
                }
            }
            playerMessages.add(new TimedMessage(messageLower));
        }

        // Set format
        String format = plugin.config.format;
        if (plugin.papiInstalled) format = PlaceholderAPI.setPlaceholders(player, format);
        format = BukkitUtility.color(format);

        // Filter message
        if (!plugin.config.filterList.isEmpty() && !player.hasPermission("chat.filter.bypass") && Boolean.FALSE.equals(plugin.config.filterMode.function.apply(new ChatEventData(event, plugin, format)))) return;

        // Disable chat reporting
        if (plugin.config.disableChatReporting) {
            event.setCancelled(true);
            final String message = format
                    .replace("%player%", player.getName())
                    .replace("%message%", event.getMessage());
            event.getRecipients().forEach(online -> online.sendMessage(message));
            Bukkit.getConsoleSender().sendMessage(message);
            return;
        }

        try {
            event.setFormat(format
                    .replace("%player%", "%1$s")
                    .replace("%message%", "%2$s"));
        } catch (final UnknownFormatConversionException e) {
            AnnoyingPlugin.log(Level.WARNING, "Formatting error! Look for any unparsed placeholders (except %player% and %message%): " + format);
        }
    }

    public static class ChatEventData {
        @NotNull public final AsyncPlayerChatEvent event;
        @NotNull public final SimpleChatFormatter plugin;
        @NotNull public final String format;

        public ChatEventData(@NotNull AsyncPlayerChatEvent event, @NotNull SimpleChatFormatter plugin, @NotNull String format) {
            this.event = event;
            this.plugin = plugin;
            this.format = format;
        }
    }

    private static class TimedMessage {
        @NotNull private final String message;
        private final long time;

        public TimedMessage(@NotNull String message) {
            this.message = message;
            this.time = System.currentTimeMillis();
        }
    }
}
