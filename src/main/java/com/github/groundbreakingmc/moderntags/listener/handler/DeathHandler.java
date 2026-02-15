package com.github.groundbreakingmc.moderntags.listener.handler;

import com.github.groundbreakingmc.moderntags.manager.PlayerTagManager;
import com.github.groundbreakingmc.moderntags.utils.PlayerLookup;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityStatus;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Handles DEATH packets to remove player tag.
 */
public final class DeathHandler implements PacketHandler {

    private final PlayerTagManager tagManager;

    public DeathHandler(@NotNull PlayerTagManager tagManager) {
        this.tagManager = tagManager;
    }

    @Override
    public void handle(@NotNull PacketSendEvent event) {
        final var packet = new WrapperPlayServerEntityStatus(event);
        if (packet.getStatus() != 3) { // not a death event (https://minecraft.wiki/w/Java_Edition_protocol/Entity_statuses#Living_Entity)
            return;
        }

        final Player viewer = event.getPlayer();
        final Player target = this.resolveTargetPlayer(packet, viewer);

        if (target != null) {
            this.tagManager.hidePlayerTag(target, viewer);
        }
    }

    private Player resolveTargetPlayer(@NotNull WrapperPlayServerEntityStatus packet, @NotNull Player viewer) {
        final boolean isSelf = viewer.getEntityId() == packet.getEntityId();
        return isSelf ? viewer : PlayerLookup.playerById(packet.getEntityId());
    }
}
