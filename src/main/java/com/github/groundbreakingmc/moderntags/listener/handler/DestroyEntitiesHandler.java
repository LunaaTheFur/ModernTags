package com.github.groundbreakingmc.moderntags.listener.handler;

import com.github.groundbreakingmc.moderntags.manager.PlayerTagManager;
import com.github.groundbreakingmc.moderntags.utils.PlayerLookup;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Handles DESTROY_ENTITIES packets to fix passenger mounting.
 */
public final class DestroyEntitiesHandler implements PacketHandler {

    private final PlayerTagManager tagManager;

    public DestroyEntitiesHandler(@NotNull PlayerTagManager tagManager) {
        this.tagManager = tagManager;
    }

    @Override
    public void handle(@NotNull PacketSendEvent event) {
        final var packet = new WrapperPlayServerDestroyEntities(event);
        final Player viewer = event.getPlayer();

        for (final int entityId : packet.getEntityIds()) {
            final Player target = PlayerLookup.playerById(entityId);

            if (target != null) {
                this.tagManager.hidePlayerTag(target, viewer);
            }
        }
    }
}
