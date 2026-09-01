package xyz.srnyx.simplechatformatter.messages;

import org.jetbrains.annotations.NotNull;
import xyz.srnyx.annoyingapi.file.okaeri.ConfigBuilder;
import xyz.srnyx.annoyingapi.message.MessagesProvider;
import xyz.srnyx.simplechatformatter.SimpleChatFormatter;


public class SCFMessagesProvider extends MessagesProvider {
    @NotNull private final SimpleChatFormatter plugin;

    public SCFMessagesProvider(@NotNull SimpleChatFormatter plugin) {
        this.plugin = plugin;

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
    public void mutateBuilder(@NotNull ConfigBuilder builder) {
        builder.config(new SCFMessages(plugin));
    }

    @Override @NotNull
    public SCFMessages get() {
        return (SCFMessages) messages;
    }
}
