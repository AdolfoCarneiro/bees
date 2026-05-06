package com.curiousbees.neoforge.capability;

import com.curiousbees.neoforge.block.GeneticApiaryBlockEntity;
import com.curiousbees.neoforge.registry.ModBlockEntities;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class ApiaryCapabilities {

    private ApiaryCapabilities() {}

    /**
     * Direction-aware item handler capability:
     *   DOWN    → output-extract-only view (hopper below pulls combs, cannot insert frames)
     *   sides   → frame-insert + output-extract view (pipes on the side can supply frames)
     */
    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.GENETIC_APIARY.get(),
                (blockEntity, side) -> side == Direction.DOWN
                        ? blockEntity.outputExtractView()
                        : blockEntity.automationOutputView());
    }
}
