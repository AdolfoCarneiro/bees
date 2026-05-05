package com.curiousbees.common.content.visual;

import java.util.Objects;
import java.util.Optional;

/**
 * Minecraft-independent visual metadata for a bee species.
 * Uses plain string IDs — no ResourceLocation or renderer classes.
 * The NeoForge client layer is responsible for resolving these IDs to textures.
 */
public final class SpeciesVisualDefinition {

    public static final String DEFAULT_MODEL_ID = "curiousbees:bee/default";

    private final String textureId;
    private final String modelId;
    private final String displayNameKey;

    private SpeciesVisualDefinition(String textureId, String modelId, String displayNameKey) {
        this.textureId = textureId;
        this.modelId = modelId;
        this.displayNameKey = displayNameKey;
    }

    /**
     * Creates a visual definition with the default bee model and a species-specific texture.
     * No display name key — use for JSON-loaded content where the key is not yet specified.
     *
     * @param textureId the texture resource path, e.g. {@code curiousbees:textures/entity/bee/meadow.png}
     */
    public static SpeciesVisualDefinition ofTexture(String textureId) {
        Objects.requireNonNull(textureId, "textureId must not be null");
        if (textureId.isBlank()) throw new IllegalArgumentException("textureId must not be blank");
        return new SpeciesVisualDefinition(textureId, DEFAULT_MODEL_ID, null);
    }

    /**
     * Creates a visual definition with the default bee model, species texture, and display name key.
     * Use for built-in species where both texture and lang key are known at compile time.
     *
     * @param textureId      the texture resource path, e.g. {@code curiousbees:textures/entity/bee/meadow.png}
     * @param displayNameKey lang key for the species name, e.g. {@code species.curiousbees.meadow}
     */
    public static SpeciesVisualDefinition ofTexture(String textureId, String displayNameKey) {
        Objects.requireNonNull(textureId, "textureId must not be null");
        Objects.requireNonNull(displayNameKey, "displayNameKey must not be null");
        if (textureId.isBlank()) throw new IllegalArgumentException("textureId must not be blank");
        if (displayNameKey.isBlank()) throw new IllegalArgumentException("displayNameKey must not be blank");
        return new SpeciesVisualDefinition(textureId, DEFAULT_MODEL_ID, displayNameKey);
    }

    /**
     * Creates a visual definition with an explicit model and texture.
     * Use this when a species needs a custom Blockbench model in the future.
     *
     * @param textureId the texture resource path
     * @param modelId   the model resource path, e.g. {@code curiousbees:bee/default}
     */
    public static SpeciesVisualDefinition of(String textureId, String modelId) {
        Objects.requireNonNull(textureId, "textureId must not be null");
        Objects.requireNonNull(modelId, "modelId must not be null");
        if (textureId.isBlank()) throw new IllegalArgumentException("textureId must not be blank");
        if (modelId.isBlank()) throw new IllegalArgumentException("modelId must not be blank");
        return new SpeciesVisualDefinition(textureId, modelId, null);
    }

    /** Texture resource path. Always non-null and non-blank. */
    public String textureId() { return textureId; }

    /** Model resource path. Defaults to {@link #DEFAULT_MODEL_ID} unless overridden. */
    public String modelId() { return modelId; }

    /**
     * Lang key for the species display name, e.g. {@code species.curiousbees.meadow}.
     * Present for built-in species; empty for JSON-loaded content without an explicit key.
     */
    public Optional<String> displayNameKey() { return Optional.ofNullable(displayNameKey); }

    /** Returns true if this definition uses the standard shared bee model. */
    public boolean usesDefaultModel() { return DEFAULT_MODEL_ID.equals(modelId); }

    @Override
    public String toString() {
        return "SpeciesVisualDefinition{textureId='" + textureId + "', modelId='" + modelId
                + "', displayNameKey='" + displayNameKey + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpeciesVisualDefinition other)) return false;
        return textureId.equals(other.textureId) && modelId.equals(other.modelId)
                && Objects.equals(displayNameKey, other.displayNameKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textureId, modelId, displayNameKey);
    }
}
