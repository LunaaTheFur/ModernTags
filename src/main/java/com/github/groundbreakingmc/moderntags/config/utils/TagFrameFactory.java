package com.github.groundbreakingmc.moderntags.config.utils;

import com.github.groundbreakingmc.moderntags.config.model.TagFrame;
import org.bukkit.Color;
import org.bukkit.entity.TextDisplay;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public final class TagFrameFactory {

    private final Map<String, BiConsumer<TagFrame.Builder, Object>> factories;

    public TagFrameFactory() {
        this.factories = new HashMap<>();
        this.registerFactories();
    }

    public TagFrame load(Map<String, Object> section) {
        final TagFrame.Builder builder = TagFrame.builder();

        for (final Map.Entry<String, Object> entry : section.entrySet()) {
            final BiConsumer<TagFrame.Builder, Object> factory = this.factories.get(entry.getKey());

            if (factory == null) {
                throw new UnsupportedOperationException("Unknown text-display property: " + entry.getKey());
            }

            factory.accept(builder, entry.getValue());
        }

        return builder.build();
    }

    private void registerFactories() {

        this.factories.put("text", (builder, value) -> {
            if (!(value instanceof String str)) {
                throw new UnsupportedOperationException("text must be String, got " + value.getClass().getCanonicalName());
            }

            builder.text(str.replace("\n", "<newline>"));
        });

        this.factories.put("x-offset", (builder, value) -> {
            if (!(value instanceof Number nmb)) {
                throw new UnsupportedOperationException("x-offset must be Number");
            }

            builder.xOffset(nmb.floatValue());
        });

        this.factories.put("y-offset", (builder, value) -> {
            if (!(value instanceof Number nmb)) {
                throw new UnsupportedOperationException("y-offset must be Number");
            }

            builder.yOffset(nmb.floatValue());
        });

        this.factories.put("z-offset", (builder, value) -> {
            if (!(value instanceof Number nmb)) {
                throw new UnsupportedOperationException("z-offset must be Number");
            }

            builder.zOffset(nmb.floatValue());
        });

        this.factories.put("scale", (builder, value) -> {
            if (!(value instanceof Number nmb)) {
                throw new UnsupportedOperationException("scale must be Number");
            }

            builder.scale(nmb.floatValue());
        });

        this.factories.put("vertical-billboard", (builder, value) -> {
            if (!(value instanceof Boolean bool)) {
                throw new UnsupportedOperationException("vertical-billboard must be Boolean");
            }

            builder.useVerticalBillboard(bool);
        });

        this.factories.put("brightness", (builder, value) -> {
            if (!(value instanceof String str)) {
                throw new UnsupportedOperationException("brightness must be String");
            }

            final String[] split = str.split("-");
            if (split.length != 2) {
                throw new UnsupportedOperationException("brightness must be specified in 'type-strength' format");
            }

            final boolean block = split[0].equals("block");
            final int strength = Integer.parseInt(split[1]);

            builder.brightness(block, strength);
        });

        this.factories.put("view-range", (builder, value) -> {
            if (!(value instanceof Number nmb)) {
                throw new UnsupportedOperationException("view-range must be Number");
            }
            builder.viewRange(nmb.floatValue());
        });

        this.factories.put("shadow-radius", (builder, value) -> {
            if (!(value instanceof Number nmb)) {
                throw new UnsupportedOperationException("shadow-radius must be Number");
            }
            builder.shadowRadius(nmb.floatValue());
        });

        this.factories.put("shadow-strength", (builder, value) -> {
            if (!(value instanceof Number nmb)) {
                throw new UnsupportedOperationException("shadow-strength must be Number");
            }
            builder.shadowStrength(nmb.floatValue());
        });

        this.factories.put("line-width", (builder, value) -> {
            if (!(value instanceof Number nmb)) {
                throw new UnsupportedOperationException("line-width must be Number");
            }
            builder.lineWidth(nmb.intValue());
        });

        this.factories.put("background-color", (builder, value) -> {
            if (value instanceof Integer nmb) {
                builder.backgroundColor(Color.fromARGB(nmb));
                return;
            }

            if (value instanceof String str) {
                final String hex = !str.isEmpty() && str.charAt(0) == '#' ? str.substring(1) : str;

                final int argb;

                if (hex.length() == 6) {
                    argb = (int) (0xFF000000L | Long.parseLong(hex, 16));
                } else if (hex.length() == 8) {
                    argb = (int) Long.parseLong(hex, 16);
                } else {
                    throw new UnsupportedOperationException(
                            "background-color must be in #RRGGBB or #AARRGGBB format"
                    );
                }

                builder.backgroundColor(Color.fromARGB(argb));
                return;
            }

            throw new UnsupportedOperationException("background-color must be hex string or number");
        });

        this.factories.put("text-opacity", (builder, value) -> {
            if (!(value instanceof Number nmb)) {
                throw new UnsupportedOperationException("text-opacity must be Number");
            }
            builder.textOpacity(nmb.byteValue());
        });

        this.factories.put("shadowed", (builder, value) -> {
            if (!(value instanceof Boolean bool)) {
                throw new UnsupportedOperationException("shadowed must be Boolean");
            }
            builder.shadowed(bool);
        });

        this.factories.put("see-through", (builder, value) -> {
            if (!(value instanceof Boolean bool)) {
                throw new UnsupportedOperationException("see-through must be Boolean");
            }
            builder.seeThrough(bool);
        });

        this.factories.put("default-background", (builder, value) -> {
            if (!(value instanceof Boolean bool)) {
                throw new UnsupportedOperationException("default-background must be Boolean");
            }
            builder.defaultBackground(bool);
        });

        this.factories.put("alignment", (builder, value) -> {
            if (!(value instanceof String str)) {
                throw new UnsupportedOperationException("alignment must be String");
            }

            builder.alignment(
                    TextDisplay.TextAlignment.valueOf(str.toUpperCase())
            );
        });
    }
}
