package com.curiousbees.neoforge.capability;

import com.curiousbees.neoforge.block.GeneticApiaryBlockEntity;
import com.curiousbees.neoforge.registry.ModBlockEntities;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class ApiaryCapabilities {

    private ApiaryCapabilities() {}

    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.GENETIC_APIARY.get(),
                (GeneticApiaryBlockEntity blockEntity, net.minecraft.core.Direction side) ->
                        blockEntity.automationOutputView());
    }
}
