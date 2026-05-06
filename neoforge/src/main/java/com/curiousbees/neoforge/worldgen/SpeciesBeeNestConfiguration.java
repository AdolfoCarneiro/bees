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
 * </ul>
 */
public record SpeciesBeeNestConfiguration(
        BlockState nestState,
        List<BlockState> attachedBlocks
) implements FeatureConfiguration {

    public static final Codec<SpeciesBeeNestConfiguration> CODEC =
            RecordCodecBuilder.create(inst -> inst.group(
                    BlockState.CODEC
                            .fieldOf("nest_state")
                            .forGetter(SpeciesBeeNestConfiguration::nestState),
                    BlockState.CODEC.listOf()
                            .optionalFieldOf("attached_blocks", List.of())
                            .forGetter(SpeciesBeeNestConfiguration::attachedBlocks)
            ).apply(inst, SpeciesBeeNestConfiguration::new));
}
