package xyz.srnyx.simplechatformatter;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.file.AnnoyingResource;

import java.util.Set;
import java.util.stream.Collectors;


public class SimpleConfig {
    @NotNull public final String format;
    public final boolean disableChatReporting;
    @NotNull public final FilterMode filterMode;
    @NotNull public final String filterCensorReplacement;
    @NotNull public final Set<String> filterList;

    public SimpleConfig(@NotNull SimpleChatFormatter plugin) {
        final AnnoyingResource config = new AnnoyingResource(plugin, "config.yml");
        format = config.getString("format", "<%player_name%> %message%");
        disableChatReporting = config.getBoolean("disable-chat-reporting", false);
        filterMode = FilterMode.match(config.getString("filter.mode", "delete"));
        filterCensorReplacement = config.getString("filter.censor-replacement", "#");
        filterList = config.getStringList("filter.list").stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }
}
