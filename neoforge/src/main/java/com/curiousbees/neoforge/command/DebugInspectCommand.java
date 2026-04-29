package com.curiousbees.neoforge.command;

import com.curiousbees.common.gameplay.analysis.GenomeReport;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** Implements the /curiousbees debug inspect_bee command. */
final class DebugInspectCommand {

    private static final double SEARCH_RADIUS = 5.0;

    private DebugInspectCommand() {}

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

        List<String> lines = GenomeReport.generate(genome.get());
        for (String line : lines) {
            source.sendSuccess(() -> Component.literal(line), false);
        }
        return 1;
    }

    private static Optional<Bee> findNearestBee(CommandSourceStack source) {
        var pos = source.getPosition();
        AABB searchBox = AABB.ofSize(pos, SEARCH_RADIUS * 2, SEARCH_RADIUS * 2, SEARCH_RADIUS * 2);

        return source.getLevel()
                .getEntitiesOfClass(Bee.class, searchBox)
                .stream()
                .min(Comparator.comparingDouble(b -> b.distanceToSqr(pos.x, pos.y, pos.z)));
    }
}
