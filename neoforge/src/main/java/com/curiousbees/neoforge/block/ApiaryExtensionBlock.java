package com.curiousbees.neoforge.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Optional extension block for the Advanced Apiary (ADR-0013).
 * Place directly above or below an {@link AdvancedApiaryBlock} to expose
 * additional automation faces. Delegates all item-handler capability queries
 * to the paired hive. The hive removes this block gracefully when broken.
 */
public final class ApiaryExtensionBlock extends BaseEntityBlock {

    public static final MapCodec<ApiaryExtensionBlock> CODEC = simpleCodec(ApiaryExtensionBlock::new);

    public ApiaryExtensionBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ApiaryExtensionBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos,
                           BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide()) {
            for (Direction dir : new Direction[]{Direction.UP, Direction.DOWN}) {
                BlockEntity neighbor = level.getBlockEntity(pos.relative(dir));
                if (neighbor instanceof AdvancedApiaryBlockEntity apiary) {
                    apiary.onExpansionChanged();
                    break;
                }
            }
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos,
                            BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && !level.isClientSide()) {
            for (Direction dir : new Direction[]{Direction.UP, Direction.DOWN}) {
                BlockEntity neighbor = level.getBlockEntity(pos.relative(dir));
                if (neighbor instanceof GeneticApiaryBlockEntity hive) {
                    hive.releaseExcessOccupants(3);
                    if (hive instanceof AdvancedApiaryBlockEntity apiary) {
                        apiary.onExpansionChanged();
                    }
                    break;
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
