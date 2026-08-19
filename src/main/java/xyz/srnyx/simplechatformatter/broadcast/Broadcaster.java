package xyz.srnyx.simplechatformatter.broadcast;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.message.BroadcastType;
import xyz.srnyx.annoyingapi.scheduler.TaskWrapper;
import xyz.srnyx.simplechatformatter.SimpleChatFormatter;
import xyz.srnyx.simplechatformatter.config.SimpleConfig;

import java.util.function.Consumer;


public class Broadcaster {
    @NotNull private final SimpleChatFormatter plugin;

    @Nullable private final DiscordBroadcaster discord;
    @Nullable private TaskWrapper typingTask;

    public Broadcaster(@NotNull SimpleChatFormatter plugin) {
        this.plugin = plugin;

        //TODO improve this (dont use plugin.config here? it forces us to create new Broadcaster whenever config reloaded)
        discord = plugin.config.broadcast.discord.enabled && Bukkit.getPluginManager().isPluginEnabled("DiscordSRV") ? new DiscordBroadcaster(plugin) : null;
    }

    public void broadcast(@NotNull AnnoyingSender sender, @NotNull String message) {
        final String name = sender.cmdSender.getName();

        // Chat message
        plugin.getMessages().get().broadcast.chat.newMessage()
                .replace("%player%", name)
                .replace("%message%", message)
                .broadcast(BroadcastType.CHAT, sender);

        // Discord message
        if (discord != null) discord.broadcast(sender, message);

        // Title
        final SimpleConfig.Broadcast.Title title = plugin.config.broadcast.title;
        if (!title.enabled) return;

        // No typing (one-time)
        if (!title.typing.enabled) {
            plugin.getMessages().get().broadcast.title.newMessage()
                    .replace("%player%", name)
                    .replace("%message%", message)
                    .broadcast(BroadcastType.FULL_TITLE, sender, title.fade_in, title.stay, title.fade_out);
            return;
        }

        // Typing
        if (typingTask != null) typingTask.cancel();
        final String messagePapiParsed = plugin.parsePapiPlaceholders(sender.getPlayerOrNull(), message);
        final int messageLength = messagePapiParsed.length();
        final long delay = title.typing.max_time.isZero() ? title.typing.delay.toMillis() : Math.min(title.typing.delay.toMillis(), title.typing.max_time.toMillis() / messageLength);
        typingTask = plugin.scheduler.runGlobalTaskTimer(new Consumer<>() {
            int character = 0;

            @Override
            public void accept(@NotNull TaskWrapper taskWrapper) {
                if (character > messageLength) {
                    taskWrapper.cancel();
                    typingTask = null;
                    return;
                }

                // Title
                plugin.getMessages().get().broadcast.title.newMessage()
                        .replace("%player%", name)
                        .replace("%message%", messagePapiParsed.substring(0, character))
                        .broadcast(BroadcastType.FULL_TITLE, sender, title.fade_in, title.stay, title.fade_out);
                // Sound
                if (title.typing.sound.enabled) Bukkit.getOnlinePlayers().forEach(title.typing.sound.sound::play);
                // Next character
                character++;
            }
        }, 0, delay / 50);
    }

    public void broadcast(@NotNull Player player, @NotNull String message) {
        broadcast(new AnnoyingSender(plugin, player), message);
    }
}
