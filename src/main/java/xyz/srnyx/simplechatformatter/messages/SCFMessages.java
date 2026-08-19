package xyz.srnyx.simplechatformatter.messages;

import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Include;
import eu.okaeri.configs.annotation.IncludePosition;
import eu.okaeri.validator.annotation.NotNull;
import xyz.srnyx.annoyingapi.file.okaeri.SubConfig;
import xyz.srnyx.annoyingapi.message.AnnoyingMessages;
import xyz.srnyx.annoyingapi.message.json.message.JsonChatMessage;
import xyz.srnyx.annoyingapi.message.json.message.JsonTitleMessage;
import xyz.srnyx.simplechatformatter.SimpleChatFormatter;


@Include(value = AnnoyingMessages.class, position = IncludePosition.BEFORE)
public class SCFMessages extends AnnoyingMessages {
    public SCFMessages(@org.jetbrains.annotations.NotNull SimpleChatFormatter plugin) {
        super(plugin);
    }

    @Comment
    @NotNull public JsonChatMessage reload = defaultMessage("%prefix%Plugin successfully reloaded!@@%p%%command%@@%command%");

    @Comment
    @Comment("Placeholders: %player%")
    @NotNull public JsonChatMessage clear = defaultMessage("%prefix%Chat has been cleared by %s%%player%%p%!");

    @Comment
    @NotNull public Broadcast broadcast = new Broadcast(this);

    @Comment
    @NotNull public Spam spam = new Spam(this);

    @Comment
    @Comment("The message sent when they try to send a message that contains a filtered word (and the mode is set to delete)")
    @Comment("Placeholders: %word%, %message%")
    @NotNull public JsonChatMessage delete = defaultMessage("%pe%Your message contained a filtered word (%se%%word%%pe%)!@@%pe%%message%@@%message%");

    public static class Broadcast extends SubConfig<SCFMessages, SCFMessages> {
        public Broadcast(@org.jetbrains.annotations.NotNull SCFMessages defaultsParent) {
            super(defaultsParent);
        }

        @Comment("Placeholders: %player%, %message%")
        @NotNull public JsonChatMessage chat = getRoot().defaultMessage("\n&8[&4&l%player%&8] &c%message%\n");

        @Comment
        @Comment("Placeholders: %player%, %message%")
        @NotNull public JsonTitleMessage title = getRoot().defaultTitle("&4&l%player%", "&c%message%");

        @Comment
        @Comment("Placeholders: %player%, %message%")
        @NotNull public JsonChatMessage discord = getRoot().defaultMessage("**BROADCAST from `%player%`**\n> %message%");

        @Comment
        @NotNull public Toggle toggle = new Toggle(this);

        public static class Toggle extends SubConfig<SCFMessages, Broadcast> {
            public Toggle(@org.jetbrains.annotations.NotNull Broadcast defaultsParent) {
                super(defaultsParent);
            }

            @Comment("Placeholders: %toggler%, %target%, %state==boolean%")
            @NotNull public JsonChatMessage toggler = getRoot().defaultMessage("%prefix%Chat broadcasting has been %s%%state==enabled//disabled%%p% for %s%%target%%p%");

            @Comment
            @Comment("Placeholders: %toggler%, %target%, %state==boolean%")
            @NotNull public JsonChatMessage target = getRoot().defaultMessage("%prefix%%s%%toggler%%p% has %s%%state==enabled//disabled%%p% chat broadcasting for you");
        }
    }

    public static class Spam extends SubConfig<SCFMessages, SCFMessages> {
        public Spam(@org.jetbrains.annotations.NotNull SCFMessages defaultsParent) {
            super(defaultsParent);
        }

        @Comment("Placeholders: %message%")
        @NotNull public JsonChatMessage speed = getRoot().defaultMessage("%pe%You're sending messages too quickly!@@%pe%%message%@@%message%");

        @Comment
        @Comment("Placeholders: %message%, %similarity%")
        @NotNull public JsonChatMessage similarity = getRoot().defaultMessage("%pe%Your message is too similar to your previous message (%se%%similarity%%%pe%)!@@%pe%%message%@@%message%");
    }
}
