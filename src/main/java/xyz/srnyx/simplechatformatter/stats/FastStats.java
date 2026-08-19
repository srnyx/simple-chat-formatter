package xyz.srnyx.simplechatformatter.stats;

import dev.faststats.Metrics;
import org.jetbrains.annotations.NotNull;
import xyz.srnyx.annoyingapi.stats.loader.FastStatsLoader;
import xyz.srnyx.simplechatformatter.SimpleChatFormatter;


public class FastStats extends FastStatsLoader {
    @NotNull private final SimpleChatFormatter plugin;

    public FastStats(@NotNull SimpleChatFormatter plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public SimpleChatFormatter getAnnoyingPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getId() {
        return "60ef416c4f0653e7d6cb884322531306";
    }

    @Override
    public void mutateMetricsFactory(@NotNull Metrics.Factory factory) {
        factory.addMetric(config("config", () -> plugin.config));
    }
}
