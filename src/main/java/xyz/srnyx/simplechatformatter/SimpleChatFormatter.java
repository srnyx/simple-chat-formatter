package xyz.srnyx.simplechatformatter;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.PluginPlatform;
import xyz.srnyx.annoyingapi.file.AnnoyingResource;


public class SimpleChatFormatter extends AnnoyingPlugin {
    @NotNull public String format = "<%player_name%> %message%";

    public SimpleChatFormatter() {
        options.commandsToRegister.add(new ChatreloadCmd(this));
        options.listenersToRegister.add(new ChatListener(this));
        options.updatePlatforms.add(PluginPlatform.modrinth("simple-chat-formatter"));
        reload();
    }

    @Override
    public void reload() {
        format = new AnnoyingResource(this, "config.yml").getString("format", "<%player_name%> %message%");
    }
}
