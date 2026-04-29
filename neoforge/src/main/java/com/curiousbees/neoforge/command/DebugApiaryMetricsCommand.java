package com.curiousbees.neoforge.command;

import com.curiousbees.common.content.products.BuiltinProductionDefinitions;
import com.curiousbees.common.gameplay.frames.FrameModifiers;
import com.curiousbees.common.gameplay.production.ProductionOutput;
import com.curiousbees.common.gameplay.production.ProductionResolver;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import com.curiousbees.neoforge.block.GeneticApiaryBlockEntity;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/** Implements /curiousbees debug apiary_metrics — samples production with active frame modifiers. */
final class DebugApiaryMetricsCommand {

    private static final int APIARY_SEARCH_RADIUS = 12;
    private static final int BEE_SEARCH_RADIUS = 8;
    private static final int SAMPLE_ROLLS = 200;
    private static final ProductionResolver RESOLVER = new ProductionResolver();

    private DebugApiaryMetricsCommand() {}

    static int execute(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();

        Optional<GeneticApiaryBlockEntity> nearestApiary = findNearestApiary(source);
        if (nearestApiary.isEmpty()) {
            source.sendFailure(Component.literal(
                    "No Genetic Apiary found within " + APIARY_SEARCH_RADIUS + " blocks."));
            return 0;
        }
        GeneticApiaryBlockEntity apiary = nearestApiary.get();

        Optional<Bee> nearestBee = findNearestBee(source);
        if (nearestBee.isEmpty()) {
            source.sendFailure(Component.literal(
                    "No bee found within " + BEE_SEARCH_RADIUS + " blocks for sampling."));
            return 0;
        }

        Optional<Genome> genome = BeeGenomeStorage.getGenome(nearestBee.get());
        if (genome.isEmpty()) {
            source.sendFailure(Component.literal("Nearest bee has no Curious Bees genome."));
            return 0;
        }

        FrameModifiers.CombinedFrameModifier frame = apiary.currentFrameModifiers();
        Map<String, Integer> outputTotals = sampleOutputs(genome.get(), frame);

        sendReport(source, apiary, frame, outputTotals);
        return 1;
    }

    private static Map<String, Integer> sampleOutputs(
            Genome genome, FrameModifiers.CombinedFrameModifier frame) {
        Map<String, Integer> totals = new HashMap<>();
        JavaGeneticRandom random = new JavaGeneticRandom(new Random());

        for (int i = 0; i < SAMPLE_ROLLS; i++) {
            var result = RESOLVER.resolve(
                    genome,
                    BuiltinProductionDefinitions.BY_SPECIES_ID,
                    random,
                    frame.productionMultiplier());
            for (ProductionOutput output : result.generatedOutputs()) {
                totals.merge(output.outputId(), output.count(), Integer::sum);
            }
        }
        return totals;
    }

    private static void sendReport(
            CommandSourceStack source,
            GeneticApiaryBlockEntity apiary,
            FrameModifiers.CombinedFrameModifier frame,
            Map<String, Integer> outputTotals) {
        source.sendSuccess(() -> Component.literal(
                "=== Apiary Metrics @ " + apiary.getBlockPos().toShortString() + " ==="), false);
        source.sendSuccess(() -> Component.literal("Sample rolls: " + SAMPLE_ROLLS), false);
        source.sendSuccess(() -> Component.literal(
                String.format("Frame multipliers -> mutation: %.3f, production: %.3f, durability/cycle: %d",
                        frame.mutationMultiplier(),
                        frame.productionMultiplier(),
                        frame.durabilityCostPerCycle())), false);

        if (outputTotals.isEmpty()) {
            source.sendSuccess(() -> Component.literal("Sample result: [no outputs generated]"), false);
            return;
        }

        outputTotals.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> source.sendSuccess(() -> Component.literal(
                        entry.getKey() + " x" + entry.getValue()), false));
    }

    private static Optional<GeneticApiaryBlockEntity> findNearestApiary(CommandSourceStack source) {
        Vec3 pos = source.getPosition();
        BlockPos center = BlockPos.containing(pos);
        GeneticApiaryBlockEntity best = null;
        double bestDistance = Double.MAX_VALUE;

        for (BlockPos candidate : BlockPos.betweenClosed(
                center.offset(-APIARY_SEARCH_RADIUS, -APIARY_SEARCH_RADIUS, -APIARY_SEARCH_RADIUS),
                center.offset(APIARY_SEARCH_RADIUS, APIARY_SEARCH_RADIUS, APIARY_SEARCH_RADIUS))) {
            if (!(source.getLevel().getBlockEntity(candidate) instanceof GeneticApiaryBlockEntity apiary)) {
                continue;
            }
            double distance = candidate.distSqr(center);
            if (distance < bestDistance) {
                best = apiary;
                bestDistance = distance;
            }
        }
        return Optional.ofNullable(best);
    }

    private static Optional<Bee> findNearestBee(CommandSourceStack source) {
        var pos = source.getPosition();
        AABB box = AABB.ofSize(pos, BEE_SEARCH_RADIUS * 2.0, BEE_SEARCH_RADIUS * 2.0, BEE_SEARCH_RADIUS * 2.0);
        return source.getLevel()
                .getEntitiesOfClass(Bee.class, box)
                .stream()
                .min(Comparator.comparingDouble(b -> b.distanceToSqr(pos.x, pos.y, pos.z)));
    }
}
