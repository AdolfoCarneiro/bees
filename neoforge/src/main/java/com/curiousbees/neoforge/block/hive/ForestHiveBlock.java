package com.curiousbees.neoforge.block.hive;

import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Natural hive for Forest Bees. Spawns in forests, birch forests, and dark forests. */
public final class ForestHiveBlock extends SpeciesHiveBlock {

    public ForestHiveBlock(BlockBehaviour.Properties properties) {
        super(BuiltinBeeSpecies.SPECIES_FOREST.id(), properties);
    }
}
