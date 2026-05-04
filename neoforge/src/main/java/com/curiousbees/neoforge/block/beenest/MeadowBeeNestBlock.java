package com.curiousbees.neoforge.block.beenest;

import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Natural bee nest for Meadow Bees. Spawns in plains, flower forests, and meadows. */
public final class MeadowBeeNestBlock extends SpeciesBeeNestBlock {

    public MeadowBeeNestBlock(BlockBehaviour.Properties properties) {
        super(BuiltinBeeSpecies.SPECIES_MEADOW.id(), properties);
    }
}
