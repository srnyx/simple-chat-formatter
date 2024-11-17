package xyz.srnyx.simplechatformatter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.file.AnnoyingResource;
import xyz.srnyx.annoyingapi.file.PlayableSound;

import java.util.Set;
import java.util.stream.Collectors;


public class SimpleConfig {
    @NotNull private final AnnoyingResource config;
    public final boolean enableFormat;
    @NotNull public final String format;
    public final boolean disableChatReporting;
    @NotNull public final Broadcast broadcast;
    @NotNull public final Spam spam;
    @NotNull public final Filter filter;

    public SimpleConfig(@NotNull SimpleChatFormatter plugin) {
        config = new AnnoyingResource(plugin, "config.yml");
        enableFormat = config.getBoolean("enable-format", true);
        format = config.getString("format", "&a%player% &8>&r %message%");
        disableChatReporting = config.getBoolean("disable-chat-reporting", false);
        broadcast = new Broadcast();
        spam = new Spam();
        filter = new Filter();
    }

    public class Broadcast {
        @Nullable public final Title title = config.getBoolean("broadcast.title.enabled", true) ? new Title() : null;
        @Nullable public final String discordChannel = config.getBoolean("broadcast.discord.enabled", true) ? config.getString("broadcast.discord.channel", "global") : null;

        public class Title {
            public final int fadeIn = config.getInt("broadcast.title.fade-in", 0);
            public final int stay = config.getInt("broadcast.title.stay", 50);
            public final int fadeOut = config.getInt("broadcast.title.fade-out", 10);
            @Nullable public final Typing typing = config.getBoolean("broadcast.title.typing.enabled", true) ? new Typing() : null;

            public class Typing {
                public final int delay = config.getInt("broadcast.title.typing.delay", 2);
                @Nullable public final PlayableSound sound = config.getPlayableSound("broadcast.title.typing.sound")
                        .filter(sound -> sound.volume > 0)
                        .orElse(null);
            }
        }
    }

    public class Spam {
        @Nullable public final Speed speed = config.getBoolean("spam.speed.enabled", true) ? new Speed() : null;
        @Nullable public final Double similarityPercent = config.getBoolean("spam.similarity.enabled", true) ? config.getDouble("spam.similarity.percent", 50) / 100 : null;

        public class Speed {
            public final int messages = config.getInt("spam.speed.messages", 3);
            public final long time = config.getInt("spam.speed.time", 1) * 1000L;
        }
    }

    public class Filter {
        @Nullable public final FilterMode mode;
        @NotNull public final String censorReplacement = config.getString("filter.censor-replacement", "#");
        @NotNull public final Set<String> list = config.getStringList("filter.list").stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        public Filter() {
            final String modeString = config.getString("filter.mode");
            mode = modeString == null || modeString.equalsIgnoreCase("disable") || list.isEmpty() ? null : FilterMode.match(modeString);
        }
    }
}
