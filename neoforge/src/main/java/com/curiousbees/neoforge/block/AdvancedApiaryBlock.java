package com.curiousbees.neoforge.block;

import com.curiousbees.neoforge.registry.ModBlockEntities;
import com.curiousbees.neoforge.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Advanced tier of the Genetic Apiary (ADR-0013).
 * Identical to the base apiary in the first-playable slice; future upgrades will
 * differentiate it via internal slots/modifiers.
 *
 * <p>An optional {@link ApiaryExtensionBlock} may be placed directly above or below
 * to expose additional automation faces. The extension is removed gracefully on break.
 */
public final class AdvancedApiaryBlock extends GeneticApiaryBlock {

    public AdvancedApiaryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AdvancedApiaryBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : createTickerHelper(type, ModBlockEntities.ADVANCED_APIARY.get(),
                        GeneticApiaryBlockEntity::serverTick);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos,
                            BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            // Remove paired extension block above/below before vanilla hive cleanup (ADR-0013).
            for (Direction dir : new Direction[]{Direction.UP, Direction.DOWN}) {
                BlockPos extPos = pos.relative(dir);
                if (level.getBlockState(extPos).is(ModBlocks.APIARY_EXTENSION.get())) {
                    Block.dropResources(level.getBlockState(extPos), level, extPos);
                    level.removeBlock(extPos, false);
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
