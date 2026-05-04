package com.curiousbees.neoforge.block.beenest;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.common.content.habitat.BeeNestCompatibilityService;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import com.curiousbees.neoforge.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

/**
 * Same storage/tick behavior as vanilla beehives, but rejects bees whose active species
 * does not match the nest block's species.
 */
public final class SpeciesBeeNestBlockEntity extends BeehiveBlockEntity {

    public SpeciesBeeNestBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.SPECIES_BEE_NEST.get();
    }

    @Override
    public void addOccupant(Entity occupant) {
        if (occupant instanceof Bee bee && level != null && !level.isClientSide()) {
            BlockState state = getBlockState();
            if (state.getBlock() instanceof SpeciesBeeNestBlock nestBlock) {
                String nestSpeciesId = nestBlock.speciesId();
                Optional<Genome> genomeOpt = BeeGenomeStorage.getGenome(bee);
                String beeSpeciesId = genomeOpt.map(g -> g.getActiveAllele(ChromosomeType.SPECIES).id()).orElse(null);
                if (beeSpeciesId == null || !BeeNestCompatibilityService.canEnter(beeSpeciesId, nestSpeciesId)) {
                    CuriousBeesMod.LOGGER.debug(
                            "Blocked bee {} (species={}) from entering {} bee nest at {}",
                            bee.getUUID(), beeSpeciesId, nestSpeciesId, getBlockPos());
                    return;
                }
            }
        }
        super.addOccupant(occupant);
    }
}
