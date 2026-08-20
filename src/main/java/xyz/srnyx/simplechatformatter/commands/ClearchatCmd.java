package xyz.srnyx.simplechatformatter.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.message.BroadcastType;
import xyz.srnyx.simplechatformatter.SimpleChatFormatter;


public class ClearchatCmd extends xyz.srnyx.simplechatformatter.commands.generated.ClearchatCmdGen {
    private static final int LINES = 100;

    public ClearchatCmd(@NotNull SimpleChatFormatter plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        for (final Player player : Bukkit.getOnlinePlayers()) if (!player.hasPermission("chat.clear.bypass")) for (int i = 0; i < LINES; i++) player.sendMessage("");
        plugin.getMessages().get().clear.newMessage()
                .replace("%player%", sender.cmdSender.getName())
                .broadcast(BroadcastType.CHAT, sender);
    }
}
