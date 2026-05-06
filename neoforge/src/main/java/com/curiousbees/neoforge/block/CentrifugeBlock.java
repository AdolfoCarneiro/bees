package com.curiousbees.neoforge.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import com.curiousbees.neoforge.registry.ModBlockEntities;

public final class CentrifugeBlock extends BaseEntityBlock {

    public static final MapCodec<CentrifugeBlock> CODEC = simpleCodec(CentrifugeBlock::new);

    public CentrifugeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CentrifugeBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CentrifugeBlockEntity centrifuge && player instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(centrifuge, buf -> buf.writeBlockPos(pos));
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : createTickerHelper(type, ModBlockEntities.CENTRIFUGE.get(),
                        CentrifugeBlockEntity::serverTick);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos,
                            BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CentrifugeBlockEntity centrifuge) {
                int totalSlots = CentrifugeBlockEntity.INPUT_SLOTS
                        + CentrifugeBlockEntity.BOTTLE_SLOTS
                        + CentrifugeBlockEntity.OUTPUT_SLOTS;
                SimpleContainer drops = new SimpleContainer(totalSlots);
                drops.setItem(0, centrifuge.inputInventory().getStackInSlot(0).copy());
                drops.setItem(1, centrifuge.bottleInventory().getStackInSlot(0).copy());
                for (int i = 0; i < CentrifugeBlockEntity.OUTPUT_SLOTS; i++) {
                    drops.setItem(2 + i, centrifuge.outputInventory().getStackInSlot(i).copy());
                }
                Containers.dropContents(level, pos, drops);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
