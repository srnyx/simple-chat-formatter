package xyz.srnyx.simplechatformatter.broadcast;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.jetbrains.annotations.NotNull;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.simplechatformatter.SimpleChatFormatter;


public class DiscordBroadcaster {
    @NotNull private final SimpleChatFormatter plugin;
    @NotNull private final DiscordSRV discordSRV = DiscordSRV.getPlugin();

    public DiscordBroadcaster(@NotNull SimpleChatFormatter plugin) {
        this.plugin = plugin;
    }

    public void broadcast(@NotNull AnnoyingSender sender, @NotNull String message) {
        final TextChannel channel = discordSRV.getDestinationTextChannelForGameChannelName(plugin.config.broadcast.discord.channel);
        if (channel != null) DiscordUtil.sendMessage(channel, plugin.getMessages().get().broadcast.discord.newMessage()
                .replace("%message%", message)
                .replace("%player%", sender.cmdSender.getName())
                .toString(sender));
    }
}
