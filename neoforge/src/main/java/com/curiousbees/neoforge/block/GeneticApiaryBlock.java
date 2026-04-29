package com.curiousbees.neoforge.block;

import com.curiousbees.neoforge.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.SimpleContainer;
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

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos,
                            BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof GeneticApiaryBlockEntity apiary) {
                int totalSlots = GeneticApiaryBlockEntity.FRAME_SLOTS + GeneticApiaryBlockEntity.OUTPUT_SLOTS;
                SimpleContainer drops = new SimpleContainer(totalSlots);
                for (int i = 0; i < apiary.frameInventory().getSlots(); i++) {
                    drops.setItem(i, apiary.frameInventory().getStackInSlot(i).copy());
                }
                int outputOffset = apiary.frameInventory().getSlots();
                for (int i = 0; i < apiary.outputInventory().getSlots(); i++) {
                    drops.setItem(outputOffset + i, apiary.outputInventory().getStackInSlot(i).copy());
                }
                Containers.dropContents(level, pos, drops);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
