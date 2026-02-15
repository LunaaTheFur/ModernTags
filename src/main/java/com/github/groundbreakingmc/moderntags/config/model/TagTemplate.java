package com.github.groundbreakingmc.moderntags.config.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TagTemplate {

    private final String key;
    private final int frameUpdateRate;
    private final int placeholdersUpdateRate;
    private final int priority;
    private final List<TagFrame> frames;

    TagTemplate(Builder builder) {
        this.key = Objects.requireNonNull(builder.key, "key can't be null");
        this.frameUpdateRate = builder.frames.size() > 1 ? builder.frameUpdateRate : -1;
        this.placeholdersUpdateRate = builder.placeholdersUpdateRate;
        this.priority = builder.priority;
        this.frames = List.copyOf(builder.frames);
    }

    public String key() {
        return this.key;
    }

    public int frameUpdateRate() {
        return this.frameUpdateRate;
    }

    public int placeholdersUpdateRate() {
        return this.placeholdersUpdateRate;
    }

    public int priority() {
        return this.priority;
    }

    public List<TagFrame> frames() {
        return this.frames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagTemplate other)) return false;
        return this.key.equals(other.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String key;
        private int frameUpdateRate = 20;
        private int placeholdersUpdateRate = 2;
        private int priority = 0;
        private final List<TagFrame> frames = new ArrayList<>();

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder frameUpdateRate(int updateRate) {
            this.frameUpdateRate = updateRate;
            return this;
        }

        public Builder placeholdersUpdateRate(int updateRate) {
            this.placeholdersUpdateRate = updateRate;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder addFrame(TagFrame tagFrame) {
            this.frames.add(tagFrame);
            return this;
        }

        public TagTemplate build() {
            return new TagTemplate(this);
        }
    }
}
