package xyz.srnyx.simplechatformatter;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.PluginPlatform;
import xyz.srnyx.annoyingapi.file.AnnoyingResource;


public class SimpleChatFormatter extends AnnoyingPlugin {
    public String format;

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

        reload();
    }

    @Override
    public void reload() {
        format = new AnnoyingResource(this, "config.yml").getString("format", "<%player_name%> %message%");
    }
}
