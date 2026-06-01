package xyz.srnyx.simplechatformatter.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.command.AnnoyingCommand;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.data.StringData;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;
import xyz.srnyx.annoyingapi.message.DefaultReplaceType;
import xyz.srnyx.annoyingapi.utility.BukkitUtility;
import xyz.srnyx.simplechatformatter.SimpleChatFormatter;

import java.util.Collection;
import java.util.List;


public class BroadcastToggleCmd extends AnnoyingCommand {
    @NotNull public static final String KEY = "broadcast";

    @NotNull private final SimpleChatFormatter plugin;

    public BroadcastToggleCmd(@NotNull SimpleChatFormatter plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public AnnoyingPlugin getAnnoyingPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getPermission() {
        return "chat.broadcast.toggle";
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        if (sender.args == null) return;

        // Self
        if (sender.args.length == 0) {
            if (!sender.checkPlayer()) return;
            toggle(sender, sender.getPlayer());
            return;
        }

        // Other
        if (!sender.checkPermission("chat.broadcast.toggle.other")) return;
        final List<OfflinePlayer> players = sender.getSelector(0, OfflinePlayer.class)
                .orElseFlatSingle(BukkitUtility::getOfflinePlayer);
        if (players != null) for (final OfflinePlayer player : players) toggle(sender, player);
    }

    @Override @Nullable
    public Collection<String> onTabComplete(@NotNull AnnoyingSender sender) {
        return sender.cmdSender.hasPermission("chat.broadcast.toggle.other")
                ? sender.withSelectorKeys(BukkitUtility.getOnlinePlayerNames(), OfflinePlayer.class)
                : null;
    }

    private void toggle(@NotNull AnnoyingSender sender, @NotNull OfflinePlayer target) {
        // Toggle
        final StringData data = new StringData(plugin, target);
        final boolean newState = !data.has(KEY);
        data.set(KEY, newState ? true : null);

        // Send message(s) TODO: does sender.cmdSender != player work?
        new AnnoyingMessage(plugin, "broadcast.toggle.toggler")
                .replace("%toggler%", target.getName())
                .replace("%target%", sender.cmdSender.getName())
                .replace("%state%", newState, DefaultReplaceType.BOOLEAN)
                .send(sender);
        if (target instanceof Player && sender.cmdSender != target) new AnnoyingMessage(plugin, "broadcast.toggle.target")
                .replace("%toggler%", sender.cmdSender.getName())
                .replace("%target%", target.getName())
                .replace("%state%", newState, DefaultReplaceType.BOOLEAN)
                .send((Player) target);
    }
}
