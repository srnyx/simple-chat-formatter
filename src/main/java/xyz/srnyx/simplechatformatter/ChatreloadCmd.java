package xyz.srnyx.simplechatformatter;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingMessage;
import xyz.srnyx.annoyingapi.command.AnnoyingCommand;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;


public class ChatreloadCmd implements AnnoyingCommand {
    @NotNull private final SimpleChatFormatter plugin;

    public ChatreloadCmd(@NotNull SimpleChatFormatter plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public SimpleChatFormatter getPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getPermission() {
        return "chat.reload";
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        plugin.reloadPlugin();
        new AnnoyingMessage(plugin, "reload").send(sender);
    }
}
