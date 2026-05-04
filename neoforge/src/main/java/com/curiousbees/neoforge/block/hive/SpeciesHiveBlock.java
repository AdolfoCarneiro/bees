package com.curiousbees.neoforge.block.hive;

import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Base class for species-specific hive blocks.
 * Extends vanilla BeehiveBlock so bee AI treats it as a valid home.
 * Entry/exit species enforcement is handled by HiveInteractionEventHandler,
 * keeping this class and its subclasses as thin wrappers.
 */
public abstract class SpeciesHiveBlock extends BeehiveBlock {

    private final String speciesId;

    protected SpeciesHiveBlock(String speciesId, BlockBehaviour.Properties properties) {
        super(properties);
        this.speciesId = speciesId;
    }

    /** The species ID that may occupy this hive, e.g. {@code curious_bees:species/meadow}. */
    public String speciesId() {
        return speciesId;
    }
}
