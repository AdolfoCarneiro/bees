package com.curiousbees.neoforge.block;

import com.curiousbees.neoforge.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Genetic Apiary block. Extends BeehiveBlock so vanilla bee AI recognizes it
 * as a valid home (bees choose it and enter/exit automatically).
 *
 * getTicker() is overridden to use our registered BlockEntityType instead of
 * BlockEntityType.BEEHIVE — vanilla's createTickerHelper checks type equality.
 */
public final class GeneticApiaryBlock extends BeehiveBlock {

    public GeneticApiaryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GeneticApiaryBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : createTickerHelper(type, ModBlockEntities.GENETIC_APIARY.get(),
                        BeehiveBlockEntity::serverTick);
    }
}
