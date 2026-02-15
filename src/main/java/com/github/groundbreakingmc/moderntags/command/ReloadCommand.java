package com.github.groundbreakingmc.moderntags.command;

import com.github.groundbreakingmc.moderntags.ModernTags;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ReloadCommand implements TabExecutor {

    private final ModernTags plugin;

    public ReloadCommand(ModernTags plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            this.plugin.reload();
            sender.sendMessage("§aPlayerTag configuration reloaded successfully!");
        } catch (Throwable th) {
            sender.sendMessage("§cFailed to reload configuration: " + th.getMessage());
            this.plugin.getLogger().severe("Error reloading configuration: " + th.getMessage());
            th.printStackTrace();
        }
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
