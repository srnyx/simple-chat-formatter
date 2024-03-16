package xyz.srnyx.simplechatformatter.listeners;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingListener;
import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;
import xyz.srnyx.annoyingapi.utility.BukkitUtility;

import xyz.srnyx.simplechatformatter.SimpleChatFormatter;

import java.util.*;
import java.util.logging.Level;


public class ChatListener extends AnnoyingListener {
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
        if (plugin.config.enableFormat && player.hasPermission("chat.format")) event.setMessage(BukkitUtility.color(event.getMessage()));

        // Spam
        if ((plugin.config.spam.speed != null || plugin.config.spam.similarity != null) && !player.hasPermission("chat.spam.bypass")) {
            final String message = event.getMessage();
            final String messageLower = message.toLowerCase();
            final List<TimedMessage> playerMessages = this.messages.computeIfAbsent(player.getUniqueId(), uuid -> new ArrayList<>());
            if (!playerMessages.isEmpty()) {
                // Similarity
                if (plugin.config.spam.similarity != null) {
                    final double similarity = playerMessages.get(playerMessages.size() - 1).similarity(messageLower);
                    if (similarity >= plugin.config.spam.similarity.percent) {
                        event.setCancelled(true);
                        new AnnoyingMessage(plugin, "spam.similarity")
                                .replace("%similarity%", (int) Math.abs(similarity * 100))
                                .replace("%message%", message)
                                .send(player);
                        return;
                    }
                }

                // Speed
                if (plugin.config.spam.speed != null) {
                    playerMessages.removeIf(msg -> msg.time < System.currentTimeMillis() - plugin.config.spam.speed.time);
                    if (playerMessages.size() + 1 > plugin.config.spam.speed.messages) {
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

        // Check if format is enabled
        if (!plugin.config.enableFormat) return;

        // Set format
        String format = plugin.config.format;
        if (plugin.papiInstalled) format = PlaceholderAPI.setPlaceholders(player, format);
        format = BukkitUtility.color(format);

        // Filter message
        if (plugin.config.filter.mode != null && !player.hasPermission("chat.filter.bypass") && Boolean.FALSE.equals(plugin.config.filter.mode.function.apply(new ChatEventData(event, plugin, format)))) return;

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

        private TimedMessage(@NotNull String message) {
            this.message = message;
            this.time = System.currentTimeMillis();
        }

        private double similarity(@NotNull String other) {
            if (message.equals(other)) return 1.0;
            final int[] mtp = matches(other);
            final double m = mtp[0];
            if (m == 0) return 0d;
            final double j = (m / message.length() + m / other.length() + (m - (double) mtp[1] / 2) / m) / 3;
            return j < 0.7d ? j : j + 0.1 * mtp[2] * (1d - j);
        }

        @Contract("_ -> new")
        private int[] matches(@NotNull String other) {
            final CharSequence max;
            final CharSequence min;
            if (message.length() > other.length()) {
                max = message;
                min = other;
            } else {
                max = other;
                min = message;
            }
            final int range = Math.max(max.length() / 2 - 1, 0);
            final int[] matchIndexes = new int[min.length()];
            Arrays.fill(matchIndexes, -1);
            final boolean[] matchFlags = new boolean[max.length()];
            int matches = 0;
            for (int mi = 0; mi < min.length(); mi++) {
                final char c1 = min.charAt(mi);
                for (int xi = Math.max(mi - range, 0), xn = Math.min(mi + range + 1, max.length()); xi < xn; xi++) if (!matchFlags[xi] && c1 == max.charAt(xi)) {
                    matchIndexes[mi] = xi;
                    matchFlags[xi] = true;
                    matches++;
                    break;
                }
            }
            final char[] ms1 = new char[matches];
            final char[] ms2 = new char[matches];
            for (int i = 0, si = 0; i < min.length(); i++) if (matchIndexes[i] != -1) {
                ms1[si] = min.charAt(i);
                si++;
            }
            for (int i = 0, si = 0; i < max.length(); i++) if (matchFlags[i]) {
                ms2[si] = max.charAt(i);
                si++;
            }
            int halfTranspositions = 0;
            for (int mi = 0; mi < ms1.length; mi++) if (ms1[mi] != ms2[mi]) halfTranspositions++;
            int prefix = 0;
            for (int mi = 0; mi < Math.min(4, min.length()); mi++) {
                if (message.charAt(mi) != other.charAt(mi)) break;
                prefix++;
            }
            return new int[]{matches, halfTranspositions, prefix};
        }
    }
}
