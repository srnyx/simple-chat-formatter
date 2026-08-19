package xyz.srnyx.simplechatformatter.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.command.AnnoyingCommand;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.message.BroadcastType;
import xyz.srnyx.simplechatformatter.SimpleChatFormatter;


public class ClearchatCmd extends AnnoyingCommand {
    private static final int LINES = 100;

    @NotNull private final SimpleChatFormatter plugin;

    public ClearchatCmd(@NotNull SimpleChatFormatter plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public AnnoyingPlugin getAnnoyingPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getPermission() {
        return "chat.clear";
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        for (final Player player : Bukkit.getOnlinePlayers()) if (!player.hasPermission("chat.clear.bypass")) for (int i = 0; i < LINES; i++) player.sendMessage("");
        plugin.getMessages().get().clear.newMessage()
                .replace("%player%", sender.cmdSender.getName())
                .broadcast(BroadcastType.CHAT, sender);
    }
}
