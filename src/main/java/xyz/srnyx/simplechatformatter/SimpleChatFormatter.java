package xyz.srnyx.simplechatformatter;

import org.bukkit.Bukkit;

import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.PluginPlatform;


public class SimpleChatFormatter extends AnnoyingPlugin {
    public SimpleConfig config;
    @Nullable public DiscordBroadcast discordBroadcast;

    public SimpleChatFormatter() {
        options
                .pluginOptions(pluginOptions -> pluginOptions.updatePlatforms(
                        PluginPlatform.modrinth("h3dHO3Sr"),
                        PluginPlatform.hangar(this),
                        PluginPlatform.spigot("111804")))
                .bStatsOptions(bStatsOptions -> bStatsOptions.id(18617))
                .registrationOptions.automaticRegistration.packages(
                        "xyz.srnyx.simplechatformatter.commands",
                        "xyz.srnyx.simplechatformatter.listeners");
    }

    @Override
    public void enable() {
        reload();
    }

    @Override
    public void reload() {
        config = new SimpleConfig(this);
        discordBroadcast = config.broadcast.discordChannel != null && Bukkit.getPluginManager().isPluginEnabled("DiscordSRV") ? new DiscordBroadcast(this) : null;
    }
}
