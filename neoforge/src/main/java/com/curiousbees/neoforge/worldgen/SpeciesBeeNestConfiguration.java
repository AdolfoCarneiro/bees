package com.curiousbees.neoforge.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

/**
 * Configuration for {@link SpeciesBeeNestFeature}.
 *
 * <ul>
 *   <li>{@code nest_state} — the block state to place as the nest.</li>
 *   <li>{@code attached_blocks} — optional list of block states scattered within 2 blocks of
 *       the nest (flowers, ferns, dead bushes, …). Each entry is one placement attempt.
 *       Omit or pass an empty list for variants that need no surrounding vegetation.</li>
 *   <li>{@code occupant_count_min} — minimum number of bees to spawn (default 2).</li>
 *   <li>{@code occupant_count_max} — maximum number of bees to spawn (default 3).</li>
 *   <li>{@code occupant_species_pool} — list of species IDs to draw from; if empty, falls
 *       back to the species ID encoded in the nest block (default empty).</li>
 * </ul>
 */
public record SpeciesBeeNestConfiguration(
        BlockState nestState,
        List<BlockState> attachedBlocks,
        int occupantCountMin,
        int occupantCountMax,
        List<String> occupantSpeciesPool
) implements FeatureConfiguration {

    public static final Codec<SpeciesBeeNestConfiguration> CODEC =
            RecordCodecBuilder.create(inst -> inst.group(
                    BlockState.CODEC
                            .fieldOf("nest_state")
                            .forGetter(SpeciesBeeNestConfiguration::nestState),
                    BlockState.CODEC.listOf()
                            .optionalFieldOf("attached_blocks", List.of())
                            .forGetter(SpeciesBeeNestConfiguration::attachedBlocks),
                    Codec.INT
                            .optionalFieldOf("occupant_count_min", 2)
                            .forGetter(SpeciesBeeNestConfiguration::occupantCountMin),
                    Codec.INT
                            .optionalFieldOf("occupant_count_max", 3)
                            .forGetter(SpeciesBeeNestConfiguration::occupantCountMax),
                    Codec.STRING.listOf()
                            .optionalFieldOf("occupant_species_pool", List.of())
                            .forGetter(SpeciesBeeNestConfiguration::occupantSpeciesPool)
            ).apply(inst, SpeciesBeeNestConfiguration::new));
}
