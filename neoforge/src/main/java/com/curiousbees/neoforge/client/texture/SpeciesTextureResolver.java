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

    /** Vanilla bee texture used as the final fallback. */
    public static final ResourceLocation VANILLA_FALLBACK =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/bee/bee.png");

    private SpeciesTextureResolver() {}

    /**
     * Resolves the texture for the given bee entity.
     * Returns the species-specific texture when available, falling back to the vanilla bee texture.
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
            LOGGER.debug("Unknown active species '{}' on bee {} — using vanilla fallback.",
                    activeSpeciesId, bee.getUUID());
            return VANILLA_FALLBACK;
        }

        Optional<SpeciesVisualDefinition> visual = species.get().visualDefinition();
        if (visual.isEmpty()) {
            LOGGER.debug("No visual definition for species '{}' — using vanilla fallback.", activeSpeciesId);
            return VANILLA_FALLBACK;
        }

        try {
            return ResourceLocation.parse(visual.get().textureId());
        } catch (Exception e) {
            LOGGER.warn("Invalid texture ID '{}' for species '{}' — using vanilla fallback.",
                    visual.get().textureId(), activeSpeciesId);
            return VANILLA_FALLBACK;
        }
    }
}
