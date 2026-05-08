package com.curiousbees.neoforge.worldgen;

import com.curiousbees.common.content.builtin.BuiltinBeeContent;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import com.curiousbees.neoforge.block.beenest.SpeciesBeeNestBlock;
import com.curiousbees.neoforge.content.NeoForgeContentRegistry;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.List;
import java.util.Random;

/**
 * Places a species-specific bee nest, populates it with 2–3 bees, and optionally
 * scatters attached vegetation blocks (flowers, ferns, dead bushes) within 2 blocks.
 * All config — including which vegetation to attach — lives in JSON, not Java.
 */
public final class SpeciesBeeNestFeature extends Feature<SpeciesBeeNestConfiguration> {

    private static final List<Direction> HORIZONTAL = List.of(
            Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);

    public SpeciesBeeNestFeature(Codec<SpeciesBeeNestConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SpeciesBeeNestConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();

        BlockPos below = origin.below();
        if (!level.getBlockState(below).isSolidRender(level, below)) return false;

        if (!level.isStateAtPosition(origin, BlockState::isAir)) return false;

        Direction facing = HORIZONTAL.get(context.random().nextInt(HORIZONTAL.size()));

        BlockState nestState = context.config().nestState()
                .setValue(BlockStateProperties.FACING, facing)
                .setValue(BlockStateProperties.LEVEL_HONEY, 0);

        if (!(nestState.getBlock() instanceof SpeciesBeeNestBlock speciesBlock)) return false;

        level.setBlock(origin, nestState, 3);

        BlockEntity be = level.getBlockEntity(origin);
        if (be instanceof BeehiveBlockEntity beehive) {
            ServerLevel serverLevel = level.getLevel();
            int minCount = context.config().occupantCountMin();
            int maxCount = context.config().occupantCountMax();
            int range = maxCount - minCount;
            int beeCount = minCount + (range > 0 ? context.random().nextInt(range + 1) : 0);

            List<String> pool = context.config().occupantSpeciesPool();
            String fallbackSpeciesId = speciesBlock.speciesId();

            for (int i = 0; i < beeCount; i++) {
                Bee bee = new Bee(EntityType.BEE, serverLevel);
                String speciesId = pool.isEmpty()
                        ? fallbackSpeciesId
                        : pool.get(context.random().nextInt(pool.size()));
                stampNestSpeciesGenome(bee, speciesId);
                beehive.addOccupant(bee);
            }
        }

        placeAttachedVegetation(level, origin, context);
        return true;
    }

    private static void placeAttachedVegetation(
            WorldGenLevel level, BlockPos origin, FeaturePlaceContext<SpeciesBeeNestConfiguration> context) {
        for (BlockState vegState : context.config().attachedBlocks()) {
            for (int attempt = 0; attempt < 3; attempt++) {
                int dx = context.random().nextInt(5) - 2;
                int dz = context.random().nextInt(5) - 2;
                BlockPos vegPos = origin.offset(dx, 0, dz);
                BlockPos belowVeg = vegPos.below();
                if (level.isStateAtPosition(vegPos, BlockState::isAir)
                        && level.getBlockState(belowVeg).isSolidRender(level, belowVeg)) {
                    level.setBlock(vegPos, vegState, 2);
                    break;
                }
            }
        }
    }

    private static void stampNestSpeciesGenome(Bee bee, String nestSpeciesId) {
        if (BeeGenomeStorage.hasGenome(bee)) return;
        NeoForgeContentRegistry.current().allSpecies().stream()
                .filter(s -> s.id().equals(nestSpeciesId))
                .findFirst()
                .ifPresent(species -> {
                    Genome genome = BuiltinBeeContent.createDefaultGenome(
                            species, new JavaGeneticRandom(new Random()));
                    BeeGenomeStorage.setGenome(bee, genome);
                });
    }
}
