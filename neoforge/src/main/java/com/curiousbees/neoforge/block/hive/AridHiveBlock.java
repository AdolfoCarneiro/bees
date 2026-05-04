package com.curiousbees.neoforge.block.hive;

import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Natural hive for Arid Bees. Spawns in deserts, savannas, and badlands. */
public final class AridHiveBlock extends SpeciesHiveBlock {

    public AridHiveBlock(BlockBehaviour.Properties properties) {
        super(BuiltinBeeSpecies.SPECIES_ARID.id(), properties);
    }
}
