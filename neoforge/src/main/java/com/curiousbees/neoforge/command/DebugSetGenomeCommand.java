package com.curiousbees.neoforge.command;

import com.curiousbees.common.content.builtin.BuiltinBeeContent;
import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/** Implements the /curiousbees debug set_bee_genome <species> command. */
final class DebugSetGenomeCommand {

    private static final double SEARCH_RADIUS = 5.0;

    /** Short-name aliases accepted by the command (e.g. "meadow" instead of full ID). */
    private static final List<String> SPECIES_SUGGESTIONS = List.of(
            "meadow", "forest", "arid", "cultivated", "hardy");

    static final SuggestionProvider<CommandSourceStack> SUGGESTIONS =
            (ctx, builder) -> SharedSuggestionProvider.suggest(SPECIES_SUGGESTIONS, builder);

    private DebugSetGenomeCommand() {}

    static int execute(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        String speciesArg = StringArgumentType.getString(ctx, "species");

        Optional<BeeSpeciesDefinition> definition = resolveSpecies(speciesArg);
        if (definition.isEmpty()) {
            source.sendFailure(Component.literal(
                    "Unknown species '" + speciesArg + "'. Valid options: "
                    + String.join(", ", SPECIES_SUGGESTIONS)));
            return 0;
        }

        Optional<Bee> nearestBee = findNearestBee(source);
        if (nearestBee.isEmpty()) {
            source.sendFailure(Component.literal(
                    "No bee found within " + (int) SEARCH_RADIUS + " blocks."));
            return 0;
        }

        Bee bee = nearestBee.get();
        Genome genome = BuiltinBeeContent.createDefaultGenome(
                definition.get(), new JavaGeneticRandom(new Random()));
        BeeGenomeStorage.setGenome(bee, genome);

        source.sendSuccess(() -> Component.literal(
                "Assigned " + definition.get().displayName()
                + " genome to bee " + bee.getUUID()), false);
        return 1;
    }

    /**
     * Resolves a species argument to a definition.
     * Accepts short names ("meadow") or full IDs ("curious_bees:species/meadow").
     */
    private static Optional<BeeSpeciesDefinition> resolveSpecies(String arg) {
        // try as full ID first
        Optional<BeeSpeciesDefinition> byId = BuiltinBeeContent.findSpecies(arg);
        if (byId.isPresent()) return byId;

        // try as short name by searching known species
        return BuiltinBeeSpecies.ALL.stream()
                .filter(def -> def.id().endsWith("/" + arg))
                .findFirst();
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
