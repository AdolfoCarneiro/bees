package com.curiousbees.neoforge.command;

import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import com.curiousbees.common.gameplay.production.ProductionOutput;
import com.curiousbees.common.gameplay.production.ProductionResolver;
import com.curiousbees.common.gameplay.production.ProductionResult;
import com.curiousbees.neoforge.content.NeoForgeContentRegistry;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.Optional;
import java.util.Random;

/** Implements /curiousbees debug roll_production — validates production from a nearby bee. */
final class DebugProductionRollCommand {

    private static final double SEARCH_RADIUS = 5.0;
    private static final ProductionResolver RESOLVER = new ProductionResolver();

    private DebugProductionRollCommand() {}

    static int execute(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();

        Optional<Bee> nearestBee = findNearestBee(source);
        if (nearestBee.isEmpty()) {
            source.sendFailure(Component.literal("No bee found within " + (int) SEARCH_RADIUS + " blocks."));
            return 0;
        }

        Bee bee = nearestBee.get();
        Optional<Genome> genome = BeeGenomeStorage.getGenome(bee);
        if (genome.isEmpty()) {
            source.sendFailure(Component.literal("This bee has no Curious Bees genome."));
            return 0;
        }

        ProductionResult result = RESOLVER.resolve(
                genome.get(),
                NeoForgeContentRegistry.current().productionBySpeciesId(),
                new JavaGeneticRandom(new Random()));

        sendResult(source, result);
        return 1;
    }

    private static void sendResult(CommandSourceStack source, ProductionResult result) {
        source.sendSuccess(() -> Component.literal("=== Production Roll ==="), false);
        source.sendSuccess(() -> Component.literal("Active:       " + result.activeSpeciesId()), false);
        source.sendSuccess(() -> Component.literal("Inactive:     " + result.inactiveSpeciesId()), false);
        source.sendSuccess(() -> Component.literal("Productivity: " + result.productivityAlleleId()), false);

        if (!result.hasOutput()) {
            source.sendSuccess(() -> Component.literal("Result:       [no output this roll]"), false);
        } else {
            for (ProductionOutput output : result.generatedOutputs()) {
                source.sendSuccess(() -> Component.literal("Output:       " + output.outputId()
                        + " x" + output.count()), false);
            }
        }
    }

    private static Optional<Bee> findNearestBee(CommandSourceStack source) {
        var pos = source.getPosition();
        AABB box = AABB.ofSize(pos, SEARCH_RADIUS * 2, SEARCH_RADIUS * 2, SEARCH_RADIUS * 2);
        return source.getLevel()
                .getEntitiesOfClass(Bee.class, box)
                .stream()
                .min(Comparator.comparingDouble(b -> b.distanceToSqr(pos.x, pos.y, pos.z)));
    }
}
