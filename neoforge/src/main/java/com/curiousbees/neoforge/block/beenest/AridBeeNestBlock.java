package com.curiousbees.neoforge.block.beenest;

import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Natural bee nest for Arid Bees. Spawns in deserts, savannas, and badlands. */
public final class AridBeeNestBlock extends SpeciesBeeNestBlock {

    public AridBeeNestBlock(BlockBehaviour.Properties properties) {
        super(BuiltinBeeSpecies.SPECIES_ARID.id(), properties);
    }
}
