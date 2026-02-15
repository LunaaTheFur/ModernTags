package com.github.groundbreakingmc.moderntags.manager;

import com.github.groundbreakingmc.moderntags.config.model.TagFrame;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Factory class for creating various packet wrappers related to player tags.
 */
public final class PacketFactory {

    private static final double TAG_Y_OFFSET = 1.8;
    private static final int TEXT_COMPONENT_INDEX = 23;

    private PacketFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates a spawn packet for a text display entity.
     *
     * @param entityId The entity ID for the text display
     * @param location The location where to spawn the entity
     * @return The spawn packet
     */
    @NotNull
    public static WrapperPlayServerSpawnEntity createSpawnPacket(int entityId, @NotNull Location location) {
        return new WrapperPlayServerSpawnEntity(
                entityId,
                Optional.of(UUID.randomUUID()),
                EntityTypes.TEXT_DISPLAY,
                new Vector3d(location.getX(), location.getY() + TAG_Y_OFFSET, location.getZ()),
                0f, 0f, 0f, 0, Optional.empty()
        );
    }

    /**
     * Creates a metadata packet for updating the text display entity.
     *
     * @param entityId The entity ID
     * @param frame    The tag frame containing styling data
     * @param text     The parsed text component to display
     * @return The metadata packet
     */
    @NotNull
    public static WrapperPlayServerEntityMetadata createMetadataPacket(int entityId, @NotNull TagFrame frame, @NotNull Component text) {
        final List<EntityData<?>> metadata = new ArrayList<>(frame.entityData());
        metadata.add(new EntityData<>(TEXT_COMPONENT_INDEX, EntityDataTypes.ADV_COMPONENT, text));
        return new WrapperPlayServerEntityMetadata(entityId, metadata);
    }

    /**
     * Creates a set passengers packet to mount the tag entity on the player.
     *
     * @param target      The player who will have passengers
     * @param tagEntityId The tag entity ID to mount
     * @return The set passengers packet
     */
    @NotNull
    public static WrapperPlayServerSetPassengers createSetPassengersPacket(@NotNull Player target, int tagEntityId) {
        final List<Entity> passengers = target.getPassengers();
        final int[] passengerIds = new int[passengers.size() + 1];
        for (int i = 0; i < passengers.size(); i++) {
            passengerIds[i] = passengers.get(i).getEntityId();
        }
        passengerIds[passengerIds.length - 1] = tagEntityId;
        return new WrapperPlayServerSetPassengers(target.getEntityId(), passengerIds);
    }

    /**
     * Creates a set passengers packet with custom passenger IDs.
     *
     * @param target       The player who will have passengers
     * @param passengerIds Array of passenger entity IDs
     * @return The set passengers packet
     */
    @NotNull
    public static WrapperPlayServerSetPassengers createSetPassengersPacket(@NotNull Player target, int[] passengerIds) {
        return new WrapperPlayServerSetPassengers(target.getEntityId(), passengerIds);
    }

    /**
     * Creates a destroy entities packet.
     *
     * @param entityIds Entity IDs to destroy
     * @return The destroy entities packet
     */
    @NotNull
    public static WrapperPlayServerDestroyEntities createDestroyPacket(int... entityIds) {
        return new WrapperPlayServerDestroyEntities(entityIds);
    }
}
