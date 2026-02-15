package com.github.groundbreakingmc.moderntags.config.model;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.Vector3f;
import com.google.common.collect.ImmutableList;
import org.bukkit.Color;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;
import java.util.List;

public class TagFrame {

    private static final Color DEFAULT_COLOR = Color.fromARGB(1073741824); // 0x40000000 (transparent)

    private final String text;
    private final List<EntityData<?>> entityData;

    TagFrame(String text, List<EntityData<?>> entityData) {
        this.text = text;
        this.entityData = ImmutableList.copyOf(entityData);
    }

    public String text() {
        return this.text;
    }

    public List<EntityData<?>> entityData() {
        return this.entityData;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String text = "";
        private float xOffset = 0f;
        private float yOffset = .2f;
        private float zOffset = 0f;
        private float scale = 1f;
        private boolean useVerticalBillboard = false;
        private int brightness = -1;
        private float viewRange = 1.0f;
        private float shadowRadius = .0f;
        private float shadowStrength = 1.0f;
        private int lineWidth = 200;
        private Color backgroundColor = TagFrame.DEFAULT_COLOR;
        private byte textOpacity = -1;
        private boolean shadowed = false;
        private boolean seeThrough = false;
        private boolean defaultBackground = false;
        private TextDisplay.TextAlignment alignment = TextDisplay.TextAlignment.CENTER;

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder xOffset(float xOffset) {
            this.xOffset = xOffset;
            return this;
        }

        public Builder yOffset(float yOffset) {
            this.yOffset = yOffset;
            return this;
        }

        public Builder zOffset(float zOffset) {
            this.zOffset = zOffset;
            return this;
        }

        public Builder scale(float scale) {
            this.scale = scale;
            return this;
        }

        public Builder useVerticalBillboard(boolean useVerticalBillboard) {
            this.useVerticalBillboard = useVerticalBillboard;
            return this;
        }

        public Builder brightness(boolean block, int brightness) {
            this.brightness = brightness << (block ? 4 : 20);
            return this;
        }

        public Builder viewRange(float viewRange) {
            this.viewRange = viewRange;
            return this;
        }

        public Builder shadowRadius(float shadowRadius) {
            this.shadowRadius = shadowRadius;
            return this;
        }

        public Builder shadowStrength(float shadowStrength) {
            this.shadowStrength = shadowStrength;
            return this;
        }

        public Builder lineWidth(int lineWidth) {
            this.lineWidth = lineWidth;
            return this;
        }

        public Builder backgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder textOpacity(byte textOpacity) {
            this.textOpacity = textOpacity;
            return this;
        }

        public Builder shadowed(boolean shadowed) {
            this.shadowed = shadowed;
            return this;
        }

        public Builder seeThrough(boolean seeThrough) {
            this.seeThrough = seeThrough;
            return this;
        }

        public Builder defaultBackground(boolean defaultBackground) {
            this.defaultBackground = defaultBackground;
            return this;
        }

        public Builder alignment(TextDisplay.TextAlignment alignment) {
            this.alignment = alignment;
            return this;
        }

        public TagFrame build() {
            final List<EntityData<?>> entityData = new ArrayList<>();
            entityData.add(new EntityData<>(11, EntityDataTypes.VECTOR3F, new Vector3f(this.xOffset, this.yOffset, this.zOffset)));
            entityData.add(new EntityData<>(12, EntityDataTypes.VECTOR3F, new Vector3f(this.scale, this.scale, this.scale)));
            entityData.add(new EntityData<>(15, EntityDataTypes.BYTE, (byte) (this.useVerticalBillboard ? 1 : 3)));
            entityData.add(new EntityData<>(16, EntityDataTypes.INT, this.brightness));
            entityData.add(new EntityData<>(17, EntityDataTypes.FLOAT, this.viewRange));
            entityData.add(new EntityData<>(18, EntityDataTypes.FLOAT, this.shadowRadius));
            entityData.add(new EntityData<>(19, EntityDataTypes.FLOAT, this.shadowStrength));
            entityData.add(new EntityData<>(24, EntityDataTypes.INT, this.lineWidth));
            entityData.add(new EntityData<>(25, EntityDataTypes.INT, this.backgroundColor.asARGB()));
            entityData.add(new EntityData<>(26, EntityDataTypes.BYTE, this.textOpacity));

            byte transformation = 0;
            if (this.shadowed) transformation |= 0x01;
            if (this.seeThrough) transformation |= 0x02;
            if (this.defaultBackground) transformation |= 0x04;
            transformation |= (byte) (alignmentToBits(this.alignment) << 3);
            entityData.add(new EntityData<>(27, EntityDataTypes.BYTE, transformation));

            return new TagFrame(this.text, entityData);
        }

        private static int alignmentToBits(TextDisplay.TextAlignment alignment) {
            return switch (alignment) {
                case CENTER -> 0; // 00
                case LEFT -> 1; // 01
                case RIGHT -> 2; // 10
            };
        }
    }
}
