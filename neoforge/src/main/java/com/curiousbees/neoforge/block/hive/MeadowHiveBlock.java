package com.curiousbees.neoforge.block.hive;

import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Natural hive for Meadow Bees. Spawns in plains, flower forests, and meadows. */
public final class MeadowHiveBlock extends SpeciesHiveBlock {

    public MeadowHiveBlock(BlockBehaviour.Properties properties) {
        super(BuiltinBeeSpecies.SPECIES_MEADOW.id(), properties);
    }
}
