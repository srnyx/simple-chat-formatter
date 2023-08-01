package xyz.srnyx.simplechatformatter;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.PluginPlatform;
import xyz.srnyx.annoyingapi.file.AnnoyingResource;


public class SimpleChatFormatter extends AnnoyingPlugin {
    @NotNull public String format = "<%player_name%> %message%";

    public SimpleChatFormatter() {
        options
                .pluginOptions(pluginOptions -> pluginOptions.updatePlatforms(
                        PluginPlatform.modrinth("simple-chat-formatter"),
                        PluginPlatform.hangar(this, "srnyx")))
                        //PluginPlatform.spigot("12345")
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
