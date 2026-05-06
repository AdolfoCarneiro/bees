package com.curiousbees.neoforge.block;

import com.curiousbees.neoforge.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

/**
 * Block entity for the Apiary Extension (ADR-0013).
 * Resolves the paired {@link AdvancedApiaryBlockEntity} dynamically by checking the
 * block directly above and below. Capability queries are forwarded to the hive's
 * existing IItemHandler views so automation behaves identically to connecting
 * directly to the advanced apiary.
 */
public final class ApiaryExtensionBlockEntity extends BlockEntity {

    public ApiaryExtensionBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.APIARY_EXTENSION.get(), pos, state);
    }

    /**
     * Finds the paired advanced apiary by checking the block directly above and below.
     * Returns null when no paired hive is present (extension functions as a no-op).
     */
    @Nullable
    public GeneticApiaryBlockEntity resolveHive() {
        if (level == null) return null;
        for (Direction dir : new Direction[]{Direction.UP, Direction.DOWN}) {
            BlockEntity neighbor = level.getBlockEntity(worldPosition.relative(dir));
            if (neighbor instanceof AdvancedApiaryBlockEntity advanced) {
                return advanced;
            }
        }
        return null;
    }

    /**
     * Returns the item handler for the given face, delegated to the paired hive.
     * Returns null when unpaired — automation pipes see no capability.
     */
    @Nullable
    public IItemHandler itemHandlerForSide(@Nullable Direction side) {
        GeneticApiaryBlockEntity hive = resolveHive();
        if (hive == null) return null;
        return side == Direction.DOWN ? hive.outputExtractView() : hive.automationOutputView();
    }
}
