package com.curiousbees.neoforge.block.beenest;

import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Natural bee nest for Forest Bees. Spawns in forests, birch forests, and dark forests. */
public final class ForestBeeNestBlock extends SpeciesBeeNestBlock {

    public ForestBeeNestBlock(BlockBehaviour.Properties properties) {
        super(BuiltinBeeSpecies.SPECIES_FOREST.id(), properties);
    }
}
