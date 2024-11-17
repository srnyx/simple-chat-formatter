package xyz.srnyx.simplechatformatter;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;


public class DiscordBroadcast {
    @NotNull private final SimpleChatFormatter plugin;
    @NotNull private final DiscordSRV discordSRV = DiscordSRV.getPlugin();

    public DiscordBroadcast(@NotNull SimpleChatFormatter plugin) {
        this.plugin = plugin;
    }

    public void broadcast(@NotNull AnnoyingSender sender, @NotNull String message) {
        final TextChannel channel = discordSRV.getDestinationTextChannelForGameChannelName(plugin.config.broadcast.discordChannel);
        if (channel != null) DiscordUtil.sendMessage(channel, new AnnoyingMessage(plugin, "broadcast.discord")
                .replace("%message%", message)
                .replace("%player%", sender.cmdSender.getName())
                .toString(sender));
    }
}
