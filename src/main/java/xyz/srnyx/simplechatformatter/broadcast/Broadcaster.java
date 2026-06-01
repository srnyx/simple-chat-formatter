package xyz.srnyx.simplechatformatter.broadcast;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.file.PlayableSound;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;
import xyz.srnyx.annoyingapi.message.BroadcastType;
import xyz.srnyx.annoyingapi.scheduler.TaskWrapper;
import xyz.srnyx.simplechatformatter.SimpleChatFormatter;
import xyz.srnyx.simplechatformatter.SimpleConfig;

import java.util.function.Consumer;


public class Broadcaster {
    @NotNull private final SimpleChatFormatter plugin;

    @Nullable private final DiscordBroadcaster discord;
    @Nullable private TaskWrapper typingTask;

    public Broadcaster(@NotNull SimpleChatFormatter plugin) {
        this.plugin = plugin;

        discord = plugin.config.broadcast.discordChannel != null && Bukkit.getPluginManager().isPluginEnabled("DiscordSRV") ? new DiscordBroadcaster(plugin) : null;
    }

    public void broadcast(@NotNull AnnoyingSender sender, @NotNull String message) {
        final String name = sender.cmdSender.getName();

        // Chat message
        new AnnoyingMessage(plugin, "broadcast.chat")
                .replace("%player%", name)
                .replace("%message%", message)
                .broadcast(BroadcastType.CHAT, sender);

        // Discord message
        if (discord != null) discord.broadcast(sender, message);

        // Title
        final SimpleConfig.Broadcast.Title title = plugin.config.broadcast.title;
        if (title == null) return;

        // No typing (one-time)
        if (title.typing == null) {
            new AnnoyingMessage(plugin, "broadcast.title")
                    .replace("%player%", name)
                    .replace("%message%", message)
                    .broadcast(BroadcastType.FULL_TITLE, sender, title.fadeIn, title.stay, title.fadeOut);
            return;
        }

        // Typing
        if (typingTask != null) typingTask.cancel();
        final String messagePapiParsed = plugin.parsePapiPlaceholders(sender.getPlayerOrNull(), message);
        final int messageLength = messagePapiParsed.length();
        final PlayableSound sound = title.typing.sound;
        int delay = title.typing.maxTime == null ? title.typing.delay : Math.min(title.typing.delay, title.typing.maxTime / messageLength);
        typingTask = plugin.scheduler.runGlobalTaskTimer(new Consumer<TaskWrapper>() {
            int character = 0;

            @Override
            public void accept(@NotNull TaskWrapper taskWrapper) {
                if (character > messageLength) {
                    taskWrapper.cancel();
                    typingTask = null;
                    return;
                }

                // Title
                new AnnoyingMessage(plugin, "broadcast.title")
                        .replace("%player%", name)
                        .replace("%message%", messagePapiParsed.substring(0, character))
                        .broadcast(BroadcastType.FULL_TITLE, sender, title.fadeIn, title.stay, title.fadeOut);
                // Sound
                if (sound != null) Bukkit.getOnlinePlayers().forEach(sound::play);
                // Next character
                character++;
            }
        }, 0, delay);
    }

    public void broadcast(@NotNull Player player, @NotNull String message) {
        broadcast(new AnnoyingSender(plugin, player), message);
    }
}
