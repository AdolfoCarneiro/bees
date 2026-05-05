package com.curiousbees.neoforge.client.texture;

import com.curiousbees.common.content.registry.ContentRegistry;
import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.content.visual.SpeciesVisualDefinition;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.neoforge.content.NeoForgeContentRegistry;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Resolves the texture ResourceLocation for a vanilla Bee entity based on its active species genome.
 * Falls back to the vanilla bee texture when genome, species, or visual profile is missing.
 */
public final class SpeciesTextureResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeciesTextureResolver.class);

    /**
     * Curious Bees generic fallback texture. DEV-PLACEHOLDER — replace with intentional "unknown species" art before release.
     * Used when a bee has a genome but its species texture is missing or invalid.
     * Falls back to vanilla texture only for bees with no Curious Bees genome.
     */
    public static final ResourceLocation MOD_FALLBACK =
            ResourceLocation.fromNamespaceAndPath("curiousbees", "textures/entity/bee/fallback.png");

    /** Vanilla bee texture — final fallback for bees with no mod genome. */
    public static final ResourceLocation VANILLA_FALLBACK =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/bee/bee.png");

    private SpeciesTextureResolver() {}

    /**
     * Resolves the texture for the given bee entity.
     *
     * <p>Fallback order:
     * <ol>
     *   <li>Species-specific texture (from visual definition).</li>
     *   <li>Mod generic fallback ({@link #MOD_FALLBACK}) — for bees with a genome but missing texture.</li>
     *   <li>{@link #VANILLA_FALLBACK} — for bees with no Curious Bees genome (allows angry/nectar variants).</li>
     * </ol>
     *
     * @param bee the vanilla Bee entity
     * @return the texture ResourceLocation to use for rendering
     */
    public static ResourceLocation resolve(Bee bee) {
        Optional<Genome> genome = BeeGenomeStorage.getGenome(bee);
        if (genome.isEmpty()) {
            return VANILLA_FALLBACK;
        }

        String activeSpeciesId = genome.get().getActiveAllele(ChromosomeType.SPECIES).id();

        ContentRegistry registry = NeoForgeContentRegistry.current();
        Optional<BeeSpeciesDefinition> species = registry.findSpecies(activeSpeciesId);
        if (species.isEmpty()) {
            LOGGER.debug("Unknown active species '{}' on bee {} — using mod fallback.",
                    activeSpeciesId, bee.getUUID());
            return MOD_FALLBACK;
        }

        Optional<SpeciesVisualDefinition> visual = species.get().visualDefinition();
        if (visual.isEmpty()) {
            LOGGER.debug("No visual definition for species '{}' — using mod fallback.", activeSpeciesId);
            return MOD_FALLBACK;
        }

        try {
            return ResourceLocation.parse(visual.get().textureId());
        } catch (Exception e) {
            LOGGER.warn("Invalid texture ID '{}' for species '{}' — using mod fallback.",
                    visual.get().textureId(), activeSpeciesId);
            return MOD_FALLBACK;
        }
    }
}
