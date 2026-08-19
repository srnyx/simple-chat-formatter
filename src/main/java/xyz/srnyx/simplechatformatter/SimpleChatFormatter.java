package xyz.srnyx.simplechatformatter;

import org.jetbrains.annotations.NotNull;
import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.simplechatformatter.broadcast.Broadcaster;
import xyz.srnyx.simplechatformatter.commands.BroadcastToggleCmd;
import xyz.srnyx.simplechatformatter.config.SimpleConfig;
import xyz.srnyx.simplechatformatter.config.migration.C0001_Nested_typing_sound;
import xyz.srnyx.simplechatformatter.messages.SCFMessagesProvider;
import xyz.srnyx.simplechatformatter.stats.FastStats;


public class SimpleChatFormatter extends AnnoyingPlugin {
    public SimpleConfig config;
    public Broadcaster broadcaster;

    public SimpleChatFormatter() {
        options
                .statsOptions(statsOptions -> statsOptions
                        .bStats(bStatsOptions -> bStatsOptions.id(18617))
                        .fastStats(fastStatsOptions -> fastStatsOptions.loader(FastStats.class)))
                .dataOptions(dataOptions -> dataOptions
                        .entityDataColumns(BroadcastToggleCmd.KEY));
    }

    @Override @NotNull
    public SCFMessagesProvider getMessages() {
        return (SCFMessagesProvider) super.getMessages();
    }

    @Override
    public void load() {
        config = configLoader.build(builder -> builder
                .config(new SimpleConfig())
                .internalStateMigrations(new C0001_Nested_typing_sound()));
        setupBroadcaster();
    }

    @Override
    public void reload() {
        config.reload();
        setupBroadcaster();
    }

    private void setupBroadcaster() {
        broadcaster = new Broadcaster(this);
    }
}
