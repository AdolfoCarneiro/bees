package com.curiousbees.neoforge.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
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
}
