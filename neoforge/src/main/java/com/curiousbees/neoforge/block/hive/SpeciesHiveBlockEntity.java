package com.curiousbees.neoforge.block.hive;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.common.content.habitat.HiveCompatibilityService;
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
 * Species hive block entity: same storage/tick behavior as vanilla beehives, but rejects
 * bees whose active species does not match the hive block's species. NeoForge 1.21.1 does
 * not ship {@code AnimalEnterLeaveHiveEvent}; entry policy is enforced here instead.
 */
public final class SpeciesHiveBlockEntity extends BeehiveBlockEntity {

    public SpeciesHiveBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.SPECIES_HIVE.get();
    }

    @Override
    public void addOccupant(Entity occupant) {
        if (occupant instanceof Bee bee && level != null && !level.isClientSide()) {
            BlockState state = getBlockState();
            if (state.getBlock() instanceof SpeciesHiveBlock hiveBlock) {
                String hiveSpeciesId = hiveBlock.speciesId();
                Optional<Genome> genomeOpt = BeeGenomeStorage.getGenome(bee);
                String beeSpeciesId = genomeOpt.map(g -> g.getActiveAllele(ChromosomeType.SPECIES).id()).orElse(null);
                if (beeSpeciesId == null || !HiveCompatibilityService.canEnter(beeSpeciesId, hiveSpeciesId)) {
                    CuriousBeesMod.LOGGER.debug(
                            "Blocked bee {} (species={}) from entering {} hive at {}",
                            bee.getUUID(), beeSpeciesId, hiveSpeciesId, getBlockPos());
                    return;
                }
            }
        }
        super.addOccupant(occupant);
    }
}
