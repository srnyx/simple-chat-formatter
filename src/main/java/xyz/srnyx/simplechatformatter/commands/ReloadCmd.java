package xyz.srnyx.simplechatformatter.commands;

import org.jetbrains.annotations.NotNull;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.simplechatformatter.SimpleChatFormatter;


public class ReloadCmd extends xyz.srnyx.simplechatformatter.commands.generated.ChatreloadCmdGen {
    public ReloadCmd(@NotNull SimpleChatFormatter plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        plugin.reloadPlugin();
        plugin.getMessages().get().reload.newMessage().send(sender);
    }
}
