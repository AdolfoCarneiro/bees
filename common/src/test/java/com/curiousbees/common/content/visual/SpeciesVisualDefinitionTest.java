package com.curiousbees.common.content.visual;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpeciesVisualDefinitionTest {

    @Test
    void ofTexture_setsTextureAndDefaultModel() {
        SpeciesVisualDefinition def = SpeciesVisualDefinition.ofTexture("curiousbees:textures/entity/bee/meadow.png");

        assertEquals("curiousbees:textures/entity/bee/meadow.png", def.textureId());
        assertEquals(SpeciesVisualDefinition.DEFAULT_MODEL_ID, def.modelId());
        assertTrue(def.usesDefaultModel());
    }

    @Test
    void of_setsTextureAndExplicitModel() {
        SpeciesVisualDefinition def = SpeciesVisualDefinition.of(
                "curiousbees:textures/entity/bee/crystal.png",
                "curiousbees:bee/crystal");

        assertEquals("curiousbees:textures/entity/bee/crystal.png", def.textureId());
        assertEquals("curiousbees:bee/crystal", def.modelId());
        assertFalse(def.usesDefaultModel());
    }

    @Test
    void ofTexture_nullTextureId_throws() {
        assertThrows(NullPointerException.class, () -> SpeciesVisualDefinition.ofTexture(null));
    }

    @Test
    void ofTexture_blankTextureId_throws() {
        assertThrows(IllegalArgumentException.class, () -> SpeciesVisualDefinition.ofTexture("  "));
    }

    @Test
    void of_nullTextureId_throws() {
        assertThrows(NullPointerException.class,
                () -> SpeciesVisualDefinition.of(null, "curiousbees:bee/default"));
    }

    @Test
    void of_nullModelId_throws() {
        assertThrows(NullPointerException.class,
                () -> SpeciesVisualDefinition.of("curiousbees:textures/entity/bee/meadow.png", null));
    }

    @Test
    void of_blankModelId_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> SpeciesVisualDefinition.of("curiousbees:textures/entity/bee/meadow.png", ""));
    }

    @Test
    void equality_sameValues_equal() {
        SpeciesVisualDefinition a = SpeciesVisualDefinition.ofTexture("curiousbees:textures/entity/bee/meadow.png");
        SpeciesVisualDefinition b = SpeciesVisualDefinition.ofTexture("curiousbees:textures/entity/bee/meadow.png");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equality_differentTexture_notEqual() {
        SpeciesVisualDefinition a = SpeciesVisualDefinition.ofTexture("curiousbees:textures/entity/bee/meadow.png");
        SpeciesVisualDefinition b = SpeciesVisualDefinition.ofTexture("curiousbees:textures/entity/bee/forest.png");
        assertNotEquals(a, b);
    }
}
