package xyz.srnyx.simplechatformatter;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.PluginPlatform;
import xyz.srnyx.simplechatformatter.broadcast.Broadcaster;
import xyz.srnyx.simplechatformatter.commands.BroadcastToggleCmd;


public class SimpleChatFormatter extends AnnoyingPlugin {
    public SimpleConfig config;
    public Broadcaster broadcaster;

    public SimpleChatFormatter() {
        options
                .pluginOptions(pluginOptions -> pluginOptions.updatePlatforms(
                        PluginPlatform.modrinth("h3dHO3Sr"),
                        PluginPlatform.hangar(this),
                        PluginPlatform.spigot("111804")))
                .dataOptions(dataOptions -> dataOptions
                        .enabled(true)
                        .entityDataColumns(BroadcastToggleCmd.KEY))
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
        broadcaster = new Broadcaster(this);
    }
}
