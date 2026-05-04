package com.curiousbees.neoforge.worldgen;

import com.curiousbees.common.content.builtin.BuiltinBeeContent;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import com.curiousbees.neoforge.block.hive.SpeciesHiveBlock;
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
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

import java.util.List;
import java.util.Random;

/**
 * World gen feature that places a species-specific hive block and populates it with 2–3 bees.
 * The hive is placed on solid ground; the facing is randomized.
 * Bees are given the hive's species genome before storage so {@link com.curiousbees.neoforge.block.hive.SpeciesHiveBlockEntity}
 * entry checks succeed (wild bees have no genome until stamped here).
 */
public final class SpeciesHiveFeature extends Feature<BlockStateConfiguration> {

    private static final List<Direction> HORIZONTAL = List.of(
            Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);

    public SpeciesHiveFeature(Codec<BlockStateConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();

        // Require solid ground below.
        BlockPos below = origin.below();
        if (!level.getBlockState(below).isSolidRender(level, below)) return false;

        // Require air at target position.
        if (!level.isStateAtPosition(origin, BlockState::isAir)) return false;

        // Pick random horizontal facing.
        Direction facing = HORIZONTAL.get(context.random().nextInt(HORIZONTAL.size()));

        BlockState hiveState = context.config().state
                .setValue(BlockStateProperties.FACING, facing)
                .setValue(BlockStateProperties.LEVEL_HONEY, 0);

        if (!(hiveState.getBlock() instanceof SpeciesHiveBlock speciesBlock)) return false;

        level.setBlock(origin, hiveState, 3);

        BlockEntity be = level.getBlockEntity(origin);
        if (be instanceof BeehiveBlockEntity beehive) {
            ServerLevel serverLevel = level.getLevel();
            String hiveSpeciesId = speciesBlock.speciesId();
            int beeCount = 2 + context.random().nextInt(2); // 2 or 3 bees
            for (int i = 0; i < beeCount; i++) {
                Bee bee = new Bee(EntityType.BEE, serverLevel);
                stampHiveSpeciesGenome(bee, hiveSpeciesId);
                beehive.addOccupant(bee);
            }
        }

        return true;
    }

    private static void stampHiveSpeciesGenome(Bee bee, String hiveSpeciesId) {
        if (BeeGenomeStorage.hasGenome(bee)) return;
        NeoForgeContentRegistry.current().allSpecies().stream()
                .filter(s -> s.id().equals(hiveSpeciesId))
                .findFirst()
                .ifPresent(species -> {
                    Genome genome = BuiltinBeeContent.createDefaultGenome(
                            species, new JavaGeneticRandom(new Random()));
                    BeeGenomeStorage.setGenome(bee, genome);
                });
    }
}
