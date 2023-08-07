package xyz.srnyx.simplechatformatter;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.file.AnnoyingResource;

import java.util.Set;
import java.util.stream.Collectors;


public class SimpleConfig {
    @NotNull public final String format;
    public final boolean disableChatReporting;
    public final boolean spamSpeedEnabled;
    public final int spamSpeedMessages;
    public final long spamSpeedTime;
    public final boolean spamSimilarityEnabled;
    public final double spamSimilarityPercent;
    @NotNull public final FilterMode filterMode;
    @NotNull public final String filterCensorReplacement;
    @NotNull public final Set<String> filterList;

    public SimpleConfig(@NotNull SimpleChatFormatter plugin) {
        final AnnoyingResource config = new AnnoyingResource(plugin, "config.yml");
        format = config.getString("format", "<%player_name%> %message%");
        disableChatReporting = config.getBoolean("disable-chat-reporting", false);
        spamSpeedEnabled = config.getBoolean("spam.speed.enabled", false);
        spamSpeedMessages = config.getInt("spam.speed.messages", 3);
        spamSpeedTime = config.getInt("spam.speed.time", 1) * 1000L;
        spamSimilarityEnabled = config.getBoolean("spam.similarity.enabled", false);
        spamSimilarityPercent = config.getDouble("spam.similarity.percent", 50) / 100;
        filterMode = FilterMode.match(config.getString("filter.mode", "delete"));
        filterCensorReplacement = config.getString("filter.censor-replacement", "#");
        filterList = config.getStringList("filter.list").stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }
}
