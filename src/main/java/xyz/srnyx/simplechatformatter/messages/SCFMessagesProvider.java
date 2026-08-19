package xyz.srnyx.simplechatformatter.messages;

import org.jetbrains.annotations.NotNull;
import xyz.srnyx.annoyingapi.message.AnnoyingMessages;
import xyz.srnyx.annoyingapi.message.MessagesProvider;
import xyz.srnyx.simplechatformatter.SimpleChatFormatter;


public class SCFMessagesProvider extends MessagesProvider {
    @NotNull private final SimpleChatFormatter plugin;
    private SCFMessages messages;

    public SCFMessagesProvider(@NotNull SimpleChatFormatter plugin) {
        this.plugin = plugin;

        builder(b -> b.config(new SCFMessages(plugin)));
        defaults
                .prefix("&5&lCHAT &8&l| &d")
                .p("&d")
                .s("&5");
    }

    @Override @NotNull
    public SimpleChatFormatter getAnnoyingPlugin() {
        return plugin;
    }

    @Override
    public void accept(@NotNull AnnoyingMessages annoyingMessages) {
        messages = (SCFMessages) annoyingMessages;
    }

    @Override @NotNull
    public SCFMessages get() {
        return messages;
    }
}
