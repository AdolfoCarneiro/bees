package com.curiousbees.common.content.data;

import java.util.Objects;

/**
 * Serializable visual metadata for a species definition.
 * Parsed from the optional "visual" field in a species JSON file.
 */
public final class SpeciesVisualData {

    private final String textureId;
    private final String modelId;

    public SpeciesVisualData(String textureId, String modelId) {
        this.textureId = Objects.requireNonNull(textureId, "textureId must not be null");
        this.modelId = modelId;
    }

    /** Convenience constructor using the default model. */
    public SpeciesVisualData(String textureId) {
        this(textureId, null);
    }

    /** Texture resource path. */
    public String textureId() { return textureId; }

    /** Model resource path, or null if the default model should be used. */
    public String modelId() { return modelId; }

    @Override
    public String toString() {
        return "SpeciesVisualData{textureId='" + textureId + "', modelId='" + modelId + "'}";
    }
}
