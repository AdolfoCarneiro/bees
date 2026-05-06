package com.curiousbees.neoforge.block.beenest;

import com.curiousbees.neoforge.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Species-specific bee nest block (vanilla bee nest shape).
 * Extends {@link BeehiveBlock} so bee AI treats nests as valid homes.
 * Entry policy is enforced by {@link SpeciesBeeNestBlockEntity}.
 * Species identity is passed at construction time — no subclass needed per species.
 */
public class SpeciesBeeNestBlock extends BeehiveBlock {

    private final String speciesId;

    public SpeciesBeeNestBlock(String speciesId, BlockBehaviour.Properties properties) {
        super(properties);
        this.speciesId = speciesId;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SpeciesBeeNestBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : createTickerHelper(type, ModBlockEntities.SPECIES_BEE_NEST.get(), BeehiveBlockEntity::serverTick);
    }

    /** The species ID that may occupy this nest, e.g. {@code curious_bees:species/meadow}. */
    public String speciesId() {
        return speciesId;
    }
}
