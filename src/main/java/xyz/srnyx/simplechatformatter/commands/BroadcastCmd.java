package xyz.srnyx.simplechatformatter.commands;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.command.AnnoyingCommand;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.file.PlayableSound;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;
import xyz.srnyx.annoyingapi.message.BroadcastType;
import xyz.srnyx.annoyingapi.utility.BukkitUtility;

import xyz.srnyx.simplechatformatter.SimpleChatFormatter;
import xyz.srnyx.simplechatformatter.SimpleConfig;

import java.util.Set;
import java.util.function.Predicate;


public class BroadcastCmd extends AnnoyingCommand {
    @NotNull private final SimpleChatFormatter plugin;
    @Nullable private BukkitTask bukkitTask;

    public BroadcastCmd(@NotNull SimpleChatFormatter plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public AnnoyingPlugin getAnnoyingPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getPermission() {
        return "chat.broadcast";
    }

    @Override @NotNull
    public Predicate<String[]> getArgsPredicate() {
        return args -> args.length >= 1;
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        if (sender.args == null) return;
        final String name = sender.cmdSender.getName();
        final String message = BukkitUtility.color(sender.getArgumentsJoined());

        // Chat message
        new AnnoyingMessage(plugin, "broadcast.chat")
                .replace("%player%", name)
                .replace("%message%", message)
                .broadcast(BroadcastType.CHAT, sender);

        // Discord message
        if (plugin.discordBroadcast != null) plugin.discordBroadcast.broadcast(sender, message);

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
        if (bukkitTask != null) bukkitTask.cancel();
        final int messageLength = message.length();
        final PlayableSound sound = title.typing.sound;
        bukkitTask = new BukkitRunnable() {
            int character = 0;
            public void run() {
                if (character > messageLength) {
                    cancel();
                    bukkitTask = null;
                    return;
                }

                // Title
                new AnnoyingMessage(plugin, "broadcast.title")
                        .replace("%player%", name)
                        .replace("%message%", message.substring(0, character))
                        .broadcast(BroadcastType.FULL_TITLE, sender, title.fadeIn, title.stay, title.fadeOut);
                // Sound
                if (sound != null) Bukkit.getOnlinePlayers().forEach(sound::play);
                // Next character
                character++;
            }
        }.runTaskTimer(plugin, 0, title.typing.delay);
    }

    @Override @Nullable
    public Set<String> onTabComplete(@NotNull AnnoyingSender sender) {
        return BukkitUtility.getOnlinePlayerNames();
    }
}
