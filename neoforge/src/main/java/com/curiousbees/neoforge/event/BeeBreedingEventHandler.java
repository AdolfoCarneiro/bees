package com.curiousbees.neoforge.event;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.common.content.builtin.BuiltinBeeMutations;
import com.curiousbees.common.gameplay.breeding.BeeBreedingOrchestrator;
import com.curiousbees.common.gameplay.breeding.BeeBreedingOutcome;
import com.curiousbees.common.gameplay.breeding.BeeBreedingRequest;
import com.curiousbees.common.genetics.breeding.BreedingService;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.mutation.MutationService;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import com.curiousbees.neoforge.bee.BeeParentResolver;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Bee;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;

import java.util.Optional;
import java.util.Random;

/**
 * Intercepts vanilla baby bee creation and assigns an inherited genome to the offspring.
 * Genetic logic is delegated entirely to common services; this handler remains thin.
 */
@EventBusSubscriber(modid = CuriousBeesMod.MOD_ID)
public final class BeeBreedingEventHandler {

    private static final BeeBreedingOrchestrator ORCHESTRATOR =
            new BeeBreedingOrchestrator(new BreedingService(), new MutationService());

    private BeeBreedingEventHandler() {}

    @SubscribeEvent
    public static void onBabyBeeSpawned(BabyEntitySpawnEvent event) {
        if (!(event.getChild() instanceof Bee child)) return;
        if (!(event.getParentA() instanceof Bee parentA)) return;
        if (!(event.getParentB() instanceof Bee parentB)) return;
        if (!(child.level() instanceof ServerLevel level)) return;

        Optional<Genome> genomeA = BeeParentResolver.resolve(parentA, level);
        Optional<Genome> genomeB = BeeParentResolver.resolve(parentB, level);

        if (genomeA.isEmpty() || genomeB.isEmpty()) {
            CuriousBeesMod.LOGGER.warn(
                    "Could not resolve genomes for both parents of baby bee {} — skipping genetics.",
                    child.getUUID());
            return;
        }

        BeeBreedingRequest request = new BeeBreedingRequest(
                genomeA.get(), genomeB.get(),
                BuiltinBeeMutations.ALL,
                new JavaGeneticRandom(new Random()));

        BeeBreedingOutcome outcome = ORCHESTRATOR.breed(request);
        BeeGenomeStorage.setGenome(child, outcome.childGenome());

        if (outcome.mutationOccurred()) {
            spawnMutationParticles(child, level);
        }
        logOutcome(child, parentA, parentB, outcome);
    }

    private static void spawnMutationParticles(Bee child, ServerLevel level) {
        level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                child.getX(), child.getY() + 0.5, child.getZ(),
                8, 0.3, 0.3, 0.3, 0.0);
    }

    private static void logOutcome(Bee child, Bee parentA, Bee parentB, BeeBreedingOutcome outcome) {
        String childSpecies = outcome.childGenome()
                .getActiveAllele(ChromosomeType.SPECIES).id();

        if (outcome.mutationOccurred()) {
            String mutatedSpecies = outcome.appliedMutation()
                    .map(m -> m.resultSpeciesAllele().id())
                    .orElse("unknown");
            CuriousBeesMod.LOGGER.info(
                    "Breeding mutation: parents {} + {} -> child {} species={} (mutated from {})",
                    parentA.getUUID(), parentB.getUUID(), child.getUUID(), childSpecies, mutatedSpecies);
        } else {
            CuriousBeesMod.LOGGER.debug(
                    "Breeding: parents {} + {} -> child {} species={}",
                    parentA.getUUID(), parentB.getUUID(), child.getUUID(), childSpecies);
        }
    }
}
