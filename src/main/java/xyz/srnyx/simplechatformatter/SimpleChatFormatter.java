package xyz.srnyx.simplechatformatter;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.PluginPlatform;


public class SimpleChatFormatter extends AnnoyingPlugin {
    @NotNull public SimpleConfig config = new SimpleConfig(this);

    public SimpleChatFormatter() {
        options
                .pluginOptions(pluginOptions -> pluginOptions.updatePlatforms(
                        PluginPlatform.modrinth("simple-chat-formatter"),
                        PluginPlatform.hangar(this, "srnyx"),
                        PluginPlatform.spigot("111804")))
                .bStatsOptions(bStatsOptions -> bStatsOptions.id(18617))
                .registrationOptions.automaticRegistration.packages(
                        "xyz.srnyx.simplechatformatter.commands",
                        "xyz.srnyx.simplechatformatter.listeners");
    }

    @Override
    public void reload() {
        config = new SimpleConfig(this);
    }
}
