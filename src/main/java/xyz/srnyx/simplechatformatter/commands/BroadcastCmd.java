package xyz.srnyx.simplechatformatter.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.utility.BukkitUtility;
import xyz.srnyx.simplechatformatter.SimpleChatFormatter;

import java.util.Set;
import java.util.function.Predicate;


public class BroadcastCmd extends xyz.srnyx.simplechatformatter.commands.generated.BroadcastCmdGen {
    public BroadcastCmd(@NotNull SimpleChatFormatter plugin) {
        super(plugin);
    }

    @Override @NotNull
    public Predicate<String[]> getArgsPredicate() {
        return args -> args.length >= 1;
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        plugin.broadcaster.broadcast(sender, BukkitUtility.color(sender.getArgumentsJoined()));
    }

    @Override @Nullable
    public Set<String> onTabComplete(@NotNull AnnoyingSender sender) {
        return BukkitUtility.getOnlinePlayerNames();
    }
}
