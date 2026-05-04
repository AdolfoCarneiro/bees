package com.curiousbees.neoforge.block.hive;

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
 * Base class for species-specific hive blocks.
 * Extends vanilla BeehiveBlock so bee AI treats it as a valid home.
 * Entry policy is enforced by {@link SpeciesHiveBlockEntity} (see {@link BeehiveBlock#getTicker}
 * pattern on {@code GeneticApiaryBlock}).
 */
public abstract class SpeciesHiveBlock extends BeehiveBlock {

    private final String speciesId;

    protected SpeciesHiveBlock(String speciesId, BlockBehaviour.Properties properties) {
        super(properties);
        this.speciesId = speciesId;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SpeciesHiveBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : createTickerHelper(type, ModBlockEntities.SPECIES_HIVE.get(), BeehiveBlockEntity::serverTick);
    }

    /** The species ID that may occupy this hive, e.g. {@code curious_bees:species/meadow}. */
    public String speciesId() {
        return speciesId;
    }
}
