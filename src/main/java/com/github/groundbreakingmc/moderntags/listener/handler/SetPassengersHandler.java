package com.github.groundbreakingmc.moderntags.listener.handler;

import com.github.groundbreakingmc.moderntags.manager.PlayerTagManager;
import com.github.groundbreakingmc.moderntags.utils.PlayerLookup;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Handles SET_PASSENGERS packets to fix tag mounting when passengers are updated.
 */
public final class SetPassengersHandler implements PacketHandler {

    private final PlayerTagManager tagManager;

    public SetPassengersHandler(@NotNull PlayerTagManager tagManager) {
        this.tagManager = tagManager;
    }

    @Override
    public void handle(@NotNull PacketSendEvent event) {
        final var packet = new WrapperPlayServerSetPassengers(event);
        final Player viewer = event.getPlayer();

        final Player target = this.resolveTargetPlayer(packet, viewer);

        if (target == null) {
            return;
        }

        if (this.tagManager.fixTagMounting(target, viewer, packet.getPassengers())) {
            event.setCancelled(true);
        }
    }

    private Player resolveTargetPlayer(@NotNull WrapperPlayServerSetPassengers packet, @NotNull Player viewer) {
        final boolean isSelf = viewer.getEntityId() == packet.getEntityId();
        return isSelf ? viewer : PlayerLookup.playerById(packet.getEntityId());
    }
}
