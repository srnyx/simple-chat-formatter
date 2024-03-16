package xyz.srnyx.simplechatformatter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.message.AnnoyingMessage;

import xyz.srnyx.simplechatformatter.listeners.ChatListener;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


public enum FilterMode {
    DELETE(data -> {
        final List<String> filteredWords = getFilteredWords(data);
        if (filteredWords.isEmpty()) return true;
        data.event.setCancelled(true);
        new AnnoyingMessage(data.plugin, "delete")
                .replace("%word%", filteredWords.get(0))
                .replace("%message%", data.event.getMessage())
                .send(data.event.getPlayer());
        return false;
    }),
    SHADOW(data -> {
        final Player player = data.event.getPlayer();
        if (getFilteredWords(data).isEmpty()) return true;
        data.event.setCancelled(true);

        // Send to player
        final String fullMessage = data.format
                .replace("%player%", player.getName())
                .replace("%message%", data.event.getMessage());
        player.sendMessage(fullMessage);

        // Send to players with permission & console
        final String shadowMessage = ChatColor.DARK_RED + "SHADOW " + ChatColor.RESET + fullMessage;
        data.event.getRecipients().stream()
                .filter(online -> !online.equals(player) && online.hasPermission("chat.filter.see"))
                .forEach(online -> online.sendMessage(shadowMessage));
        Bukkit.getConsoleSender().sendMessage(shadowMessage);
        return false;
    }),
    CENSOR(data -> {
        final List<String> filteredWords = getFilteredWords(data);
        if (filteredWords.isEmpty()) return true;
        data.event.setCancelled(true);
        final Player player = data.event.getPlayer();
        final String formatPlayer = data.format.replace("%player%", player.getName());

        // Send censored message
        final StringBuilder censorBuilder = new StringBuilder(data.event.getMessage());
        final char replacement = data.plugin.config.filter.censorReplacement.charAt(0);
        for (final String word : filteredWords) {
            final int start = censorBuilder.indexOf(word);
            final int end = start + word.length();
            for (int i = start; i < end; i++) censorBuilder.setCharAt(i, replacement);
        }
        final String censoredMessage = formatPlayer.replace("%message%", censorBuilder.toString());
        data.event.getRecipients().stream()
                .filter(online -> !online.hasPermission("chat.filter.see"))
                .forEach(online -> online.sendMessage(censoredMessage));

        // Send highlighted message
        final StringBuilder highlightBuilder = new StringBuilder(data.event.getMessage());
        for (final String word : filteredWords) {
            final int start = highlightBuilder.indexOf(word);
            highlightBuilder.insert(start, ChatColor.RED);
            highlightBuilder.insert(start + word.length() + 2, ChatColor.RESET + ChatColor.getLastColors(highlightBuilder.substring(0, start)));
        }
        final String highlightedMessage = formatPlayer.replace("%message%", highlightBuilder.toString());
        data.event.getRecipients().stream()
                .filter(online -> online.hasPermission("chat.filter.see"))
                .forEach(online -> online.sendMessage(highlightedMessage));
        Bukkit.getConsoleSender().sendMessage(highlightedMessage);

        return false;
    });

    @NotNull public final Function<ChatListener.ChatEventData, Boolean> function;

    FilterMode(@NotNull Function<ChatListener.ChatEventData, Boolean> function) {
        this.function = function;
    }

    @NotNull
    public static FilterMode match(@Nullable String string) {
        if (string == null) return DELETE;
        try {
            return valueOf(string.toUpperCase());
        } catch (final IllegalArgumentException exception) {
            return DELETE;
        }
    }

    @NotNull
    private static List<String> getFilteredWords(@NotNull ChatListener.ChatEventData data) {
        final Set<String> filterList = data.plugin.config.filter.list;
        return Arrays.stream(data.event.getMessage().toLowerCase().replaceAll("[^ a-z0-9]", "").split(" "))
                .filter(filterList::contains)
                .collect(Collectors.toList());
    }
}
