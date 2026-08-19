package xyz.srnyx.simplechatformatter.config;

import com.cryptomorin.xseries.XSound;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.serdes.commons.duration.DurationSpec;
import eu.okaeri.validator.annotation.Max;
import eu.okaeri.validator.annotation.Min;
import eu.okaeri.validator.annotation.NotNull;
import xyz.srnyx.annoyingapi.file.PlayableSound;
import xyz.srnyx.annoyingapi.file.okaeri.RootConfig;
import xyz.srnyx.annoyingapi.file.okaeri.SubConfig;
import xyz.srnyx.annoyingapi.file.okaeri.serdes.duration.DurationTickFallback;
import xyz.srnyx.annoyingapi.stats.Stat;
import xyz.srnyx.simplechatformatter.FilterMode;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


@Header("DOCUMENTATION: https://annoying-api.srnyx.com/wiki/File-objects")
public class SimpleConfig extends RootConfig {
    @Comment
    @Comment
    @Comment("If false, the plugin won't attempt to format the chat at all, useful if you just want to use the other features")
    @Comment("DISABLING THIS WILL ALSO DISABLE: disable_chat_reporting, filter")
    @Stat
    public boolean enable_format = true;

    @Comment("PlaceholderAPI placeholders and formatting codes are supported")
    @Comment("The only default placeholders are %player% and %message%")
    @NotNull public String format = "&a%player% &8>&r %message%";

    @Comment
    @Comment("1.19.1+ Remove the ability for people to report other player's messages")
    @Comment("This may break other chat plugins, if you need it to not break them, let me know here: https://simple-chat-formatter.srnyx.com/issues/new/choose")
    @Stat
    public boolean disable_chat_reporting = false;

    @Comment
    @Comment("A simple broadcast system with a few extra features")
    @NotNull public Broadcast broadcast = new Broadcast(this);

    @Comment
    @Comment("A simple chat spam filter with a few different modes")
    @Comment("If a player has the chat.spam.bypass permission, they won't be affected by the filter")
    @NotNull public Spam spam = new Spam(this);

    @Comment
    @Comment("A simple chat/word filter with a few different modes")
    @Comment("If a player has the chat.filter.bypass permission, they won't be affected by the filter")
    @NotNull public Filter filter = new Filter(this);

    public static class Broadcast extends SubConfig<SimpleConfig, SimpleConfig> {
        public Broadcast(@org.jetbrains.annotations.NotNull SimpleConfig defaultsParent) {
            super(defaultsParent);
        }

        @Comment("If enabled, a title of the broadcast will be sent to all players when one is sent")
        @NotNull public Title title = new Title(this);

        @Comment
        @Comment("Options for sending broadcasts to Discord using DiscordSRV")
        @NotNull public Discord discord = new Discord(this);

        public static class Title extends SubConfig<SimpleConfig, Broadcast> {
            public Title(@org.jetbrains.annotations.NotNull Broadcast defaultsParent) {
                super(defaultsParent);
            }

            @Stat
            public boolean enabled = true;

            @Comment
            @Comment("The fade-in time for the title")
            @Stat
            public int fade_in = 0;

            @Comment("The stay time for the title")
            @Stat
            public int stay = 50;

            @Comment("The fade-out time for the title")
            @Stat
            public int fade_out = 10;

            @Comment
            @Comment("If enabled, the broadcast will be \"typed out\" letter by letter in the title")
            @NotNull public Typing typing = new Typing(this);

            public static class Typing extends SubConfig<SimpleConfig, Title> {
                public Typing(@org.jetbrains.annotations.NotNull Title defaultsParent) {
                    super(defaultsParent);
                }

                @Stat
                public boolean enabled = true;

                @Comment("The delay between each letter being typed out")
                @DurationTickFallback @Stat
                @NotNull public Duration delay = Duration.ofMillis(100);

                @Comment("The maximum time the broadcast will be typed out for")
                @Comment("So if length * delay > max-time, the delay will be automatically reduced until it fits")
                @DurationTickFallback @Stat
                @NotNull public Duration max_time = Duration.ZERO;

                @Comment
                @Comment("The sound that will be played for each letter")
                @NotNull public Sound sound = new Sound(this);

                public static class Sound extends SubConfig<SimpleConfig, Typing> {
                    public Sound(@org.jetbrains.annotations.NotNull Typing defaultsParent) {
                        super(defaultsParent);
                    }

                    @Stat
                    public boolean enabled = true;

                    @Comment("SOUND (see docs)")
                    @Stat
                    @NotNull public PlayableSound sound = new PlayableSound(XSound.UI_BUTTON_CLICK, 1, 2);
                }
            }
        }

        public static class Discord extends SubConfig<SimpleConfig, Broadcast> {
            public Discord(@org.jetbrains.annotations.NotNull Broadcast defaultsParent) {
                super(defaultsParent);
            }

            @Stat
            public boolean enabled = true;

            @Comment("The DiscordSRV channel to send broadcasts to (see 'Channels' in DiscordSRV's config.yml)")
            @NotNull public String channel = "global";
        }
    }

    public static class Spam extends SubConfig<SimpleConfig, SimpleConfig> {
        public Spam(@org.jetbrains.annotations.NotNull SimpleConfig defaultsParent) {
            super(defaultsParent);
        }

        @NotNull public Speed speed = new Speed(this);

        @Comment
        @NotNull public Similarity similarity = new Similarity(this);

        public static class Speed extends SubConfig<SimpleConfig, Spam> {
            public Speed(@org.jetbrains.annotations.NotNull Spam defaultsParent) {
                super(defaultsParent);
            }

            @Comment("Whether to filter messages sent too quickly")
            @Stat
            public boolean enabled = true;

            @Comment("The amount of messages that can be sent in the spam.time before the player's messages are deleted")
            @Stat
            public int messages = 3;

            @Comment("The amount of seconds that the player has to send spam.messages messages before their messages are deleted")
            @DurationSpec(fallbackUnit = ChronoUnit.SECONDS) @Stat
            @NotNull public Duration time = Duration.ofSeconds(1);
        }

        public static class Similarity extends SubConfig<SimpleConfig, Spam> {
            public Similarity(@org.jetbrains.annotations.NotNull Spam defaultsParent) {
                super(defaultsParent);
            }

            @Comment("Whether to filter messages that are too similar to the previous message")
            @Stat
            public boolean enabled = true;

            @Comment("How similar a message has to be to the previous message to be considered spam")
            @Comment("0-100, 0 being not similar at all, 100 being exactly the same")
            @Min(0) @Max(100) @Stat
            public int percent = 80;

            public double getPercent() {
                return percent / 100d;
            }
        }
    }

    public static class Filter extends SubConfig<SimpleConfig, SimpleConfig> {
        public Filter(@org.jetbrains.annotations.NotNull SimpleConfig defaultsParent) {
            super(defaultsParent);
        }

        @Comment("The mode that the filter will use")
        @Comment("DISABLE: Disables the word filter")
        @Comment("DELETE: Deletes/hides the message for everyone, sending an error ('delete' in messages.yml) to the player")
        @Comment("SHADOW: Deletes/hides the message for everyone except the player")
        @Comment("CENSOR: Replaces the filtered words' letters with the filter.censor-replacement value")
        @Stat
        @NotNull public FilterMode mode = FilterMode.DISABLE;

        @Comment
        @Comment("The replacement to use for each filtered word letter when filter.mode is set to censor")
        @Comment("This can only be one character!")
        @Stat
        public char censor_replacement = '#';

        @Comment("The list of words that will be filtered (case-insensitive, alphanumeric only)")
        @Stat(sizeOnly = true)
        @NotNull public Set<String> list = new LinkedHashSet<>(List.of("badword1", "badword2", "badword3"));
    }
}
