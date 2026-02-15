package com.github.groundbreakingmc.moderntags.listener.handler;

import com.github.groundbreakingmc.moderntags.manager.PlayerTagManager;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoRemove;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Handles PLAYER_INFO_REMOVE packets to hide tags when players are removed from tab list.
 */
public final class PlayerInfoRemoveHandler implements PacketHandler {

    private final PlayerTagManager tagManager;

    public PlayerInfoRemoveHandler(@NotNull PlayerTagManager tagManager) {
        this.tagManager = tagManager;
    }

    @Override
    public void handle(@NotNull PacketSendEvent event) {
        final var packet = new WrapperPlayServerPlayerInfoRemove(event);
        final Player viewer = event.getPlayer();

        for (final UUID uuid : packet.getProfileIds()) {
            final Player target = Bukkit.getPlayer(uuid);
            if (target != null) {
                this.tagManager.hidePlayerTag(target, viewer);
            }
        }
    }
}
