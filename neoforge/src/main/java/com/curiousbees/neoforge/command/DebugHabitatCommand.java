package com.curiousbees.neoforge.command;

import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import com.curiousbees.common.content.habitat.HabitatPredicate;
import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.gameplay.spawn.WildBeeSpawnService;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implements /curiousbees habitat here.
 * Prints the biome tags, Y, and light at the executor's position, then evaluates
 * every world-spawnable species' HabitatPredicate to show which would win spawn selection.
 */
final class DebugHabitatCommand {

    private DebugHabitatCommand() {}

    static int execute(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        BlockPos pos = BlockPos.containing(source.getPosition());
        var level = source.getLevel();

        Holder<Biome> biomeHolder = level.getBiome(pos);
        String biomeId = biomeHolder.unwrapKey()
                .map(k -> k.location().toString())
                .orElse("unknown");

        List<String> biomeTags = biomeHolder.tags()
                .map(tagKey -> tagKey.location().toString())
                .collect(Collectors.toList());

        int y     = pos.getY();
        int light = Math.max(
                level.getBrightness(LightLayer.SKY, pos),
                level.getBrightness(LightLayer.BLOCK, pos));

        source.sendSuccess(() -> Component.literal("=== Habitat @ " + pos.toShortString() + " ==="), false);
        source.sendSuccess(() -> Component.literal("Biome: " + biomeId), false);
        source.sendSuccess(() -> Component.literal("Tags:  " + (biomeTags.isEmpty() ? "(none)" : String.join(", ", biomeTags))), false);
        source.sendSuccess(() -> Component.literal("Y=" + y + "  Light=" + light), false);
        source.sendSuccess(() -> Component.literal("--- Predicate evaluation ---"), false);

        List<String> matchLines = new ArrayList<>();
        List<String> noMatchLines = new ArrayList<>();

        for (BeeSpeciesDefinition species : BuiltinBeeSpecies.ALL) {
            species.habitat().flatMap(h -> h.spawnPredicate()).ifPresent(predicate -> {
                boolean tagMatch   = predicate.matchesBiomeTags(biomeTags);
                boolean yMatch     = predicate.matchesY(y);
                boolean lightMatch = predicate.matchesLight(light);
                boolean overall    = tagMatch && yMatch && lightMatch;

                String wildcard = predicate.requiredBiomeTags().isEmpty() ? " [wildcard]" : "";
                String line = (overall ? "[MATCH]    " : "[no match] ")
                        + species.id() + wildcard
                        + "  tags=" + tagMatch
                        + " y=" + yMatch
                        + " light=" + lightMatch;

                if (overall) matchLines.add(line);
                else noMatchLines.add(line);
            });
        }

        for (String line : matchLines)   source.sendSuccess(() -> Component.literal(line), false);
        for (String line : noMatchLines) source.sendSuccess(() -> Component.literal(line), false);

        BeeSpeciesDefinition selected = WildBeeSpawnService.speciesForHabitat(biomeTags, y, light);
        source.sendSuccess(() -> Component.literal("Selected: " + selected.id()), false);

        return 1;
    }
}
