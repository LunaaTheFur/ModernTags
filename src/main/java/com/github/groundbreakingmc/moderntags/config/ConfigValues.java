package com.github.groundbreakingmc.moderntags.config;

import com.github.groundbreakingmc.moderntags.config.model.TagTemplate;
import com.github.groundbreakingmc.moderntags.config.utils.TagFrameFactory;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class ConfigValues {

    private final Logger logger;
    private final Path configPath;
    private final TagFrameFactory tagFrameFactory;

    private boolean useMinimessageColorizer;
    private boolean hideTagWhenHasPassenger;
    private Map<String, TagTemplate> tags;

    public ConfigValues(@NotNull Logger logger, @NotNull Path configPath) {
        this.logger = logger;
        this.configPath = configPath;
        this.tagFrameFactory = new TagFrameFactory();
    }

    public void setup() {
        final ConfigurationNode root = this.loadRoot();

        this.unregisterOldPermissions();
        this.loadSettings(root);
        this.loadNameTags(root);
        this.registerPermissions();
        this.validateConfiguration();
    }

    public boolean useMinimessageColorizer() {
        return this.useMinimessageColorizer;
    }

    public boolean hideTagWhenHasPassenger() {
        return this.hideTagWhenHasPassenger;
    }

    public Map<String, TagTemplate> tags() {
        return this.tags;
    }

    private void loadSettings(ConfigurationNode root) {
        this.useMinimessageColorizer = root.node("use-minimessage-colorizer-for-prefixes-and-suffixes").getBoolean();
        this.hideTagWhenHasPassenger = root.node("hide-tag-when-has-passenger").getBoolean();
    }

    private void unregisterOldPermissions() {
        Bukkit.getServer().getPluginManager().getPermissions()
                .stream()
                .filter(perm -> perm.getName().startsWith("moderntags.tag."))
                .forEach(perm -> Bukkit.getServer().getPluginManager().removePermission(perm));
    }

    private void registerPermissions() {
        this.tags.values().forEach(template -> {
            final String permission = "moderntags.tag." + template.key();
            try {
                Bukkit.getServer().getPluginManager().addPermission(
                        new Permission(permission, PermissionDefault.FALSE)
                );
                this.logger.info("Registered permission: " + permission);
            } catch (IllegalArgumentException ex) {
                this.logger.warning("Permission already exists: " + permission);
            }
        });
    }

    private void validateConfiguration() {
        if (this.tags == null || this.tags.isEmpty()) {
            throw new IllegalStateException("No tags configured");
        }

        if (!this.tags.containsKey("default")) {
            throw new IllegalStateException("Default tag is required");
        }

        // Validate each tag has at least one frame
        this.tags.forEach((key, template) -> {
            if (template.frames().isEmpty()) {
                throw new IllegalStateException("Tag '" + key + "' has no frames configured");
            }
        });

        this.logger.info("Configuration validated successfully. Loaded " + this.tags.size() + " tags.");
    }

    private void loadNameTags(ConfigurationNode root) {
        final Map<String, TagTemplate> result = new HashMap<>();

        final ConfigurationNode nameTagsNode = root.node("name-tags");
        if (nameTagsNode.empty()) {
            throw new IllegalStateException("No name-tags section found in config");
        }

        nameTagsNode.childrenMap().forEach((rawKey, tagNode) -> {
            final String key = String.valueOf(rawKey);

            try {
                final TagTemplate.Builder builder = TagTemplate.builder()
                        .key(key)
                        .frameUpdateRate(tagNode.node("frame-update-rate").getInt(-1))
                        .placeholdersUpdateRate(tagNode.node("placeholders-update-rate").getInt(-1))
                        .priority(tagNode.node("priority").getInt(0));

                final ConfigurationNode framesNode = tagNode.node("frames");
                if (framesNode.empty()) {
                    throw new IllegalStateException("Tag '" + key + "' has no frames");
                }

                framesNode.childrenMap().forEach((frameKey, frameNode) -> {
                    try {
                        builder.addFrame(this.tagFrameFactory.load(this.serialize(frameNode)));
                    } catch (Exception e) {
                        this.logger.severe("Error loading frame '" + frameKey + "' in tag '" + key + "': " + e.getMessage());
                        throw e;
                    }
                });

                result.put(key, builder.build());
                this.logger.info("Loaded tag: " + key + " with " + builder.build().frames().size() + " frames");
            } catch (Exception e) {
                this.logger.severe("Error loading tag '" + key + "': " + e.getMessage());
                throw new RuntimeException("Failed to load tag: " + key, e);
            }
        });

        this.tags = ImmutableMap.copyOf(result);
    }

    private Map<String, Object> serialize(ConfigurationNode section) {
        final Map<Object, ? extends ConfigurationNode> children = section.childrenMap();
        final Map<String, Object> result = new HashMap<>(children.size());

        for (final Map.Entry<Object, ? extends ConfigurationNode> entry : children.entrySet()) {
            final String key = entry.getKey().toString();
            final Object value = entry.getValue().raw();
            result.put(key, value);
        }

        return result;
    }

    private ConfigurationNode loadRoot() {
        try {
            final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                    .path(this.configPath)
                    .build();

            return loader.load();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load config from: " + this.configPath, ex);
        }
    }
}
