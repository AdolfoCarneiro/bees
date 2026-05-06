package com.curiousbees.neoforge.block;

import com.curiousbees.neoforge.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Block entity for the Advanced Apiary (ADR-0013).
 * All inventory, production, and automation logic is inherited from
 * {@link GeneticApiaryBlockEntity}. This class exists solely to bind the
 * advanced apiary to its own registered BlockEntityType so NBT round-trips correctly.
 */
public final class AdvancedApiaryBlockEntity extends GeneticApiaryBlockEntity {

    public AdvancedApiaryBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.ADVANCED_APIARY.get();
    }
}
