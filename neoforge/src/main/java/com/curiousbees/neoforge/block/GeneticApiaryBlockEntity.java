package com.curiousbees.neoforge.block;

import com.curiousbees.neoforge.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * Block entity for the Genetic Apiary.
 *
 * Extends BeehiveBlockEntity so vanilla bee AI (instanceof checks) recognizes
 * it as a valid hive. getType() is overridden so NBT serialization uses our
 * registered type (curiousbees:genetic_apiary) instead of minecraft:beehive.
 *
 * Production logic (intercepting bee-enters-with-nectar) is added in Phase 7G.
 */
public final class GeneticApiaryBlockEntity extends BeehiveBlockEntity {

    public static final int OUTPUT_SLOTS = 6;

    private final ItemStackHandler outputInventory = new ItemStackHandler(OUTPUT_SLOTS);

    public GeneticApiaryBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    /**
     * Override so NBT saves "curiousbees:genetic_apiary" instead of "minecraft:beehive".
     * BeehiveBlockEntity hardcodes BlockEntityType.BEEHIVE in its constructor; overriding
     * this method is the correct way to redirect serialization without a mixin.
     */
    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.GENETIC_APIARY.get();
    }

    public ItemStackHandler outputInventory() {
        return outputInventory;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("OutputInventory", outputInventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("OutputInventory")) {
            outputInventory.deserializeNBT(registries, tag.getCompound("OutputInventory"));
        }
    }
}
