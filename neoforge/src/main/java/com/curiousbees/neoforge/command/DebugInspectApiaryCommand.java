package com.curiousbees.neoforge.command;

import com.curiousbees.neoforge.block.GeneticApiaryBlockEntity;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Optional;

/** Implements /curiousbees debug inspect_apiary — inspects nearest apiary output inventory. */
final class DebugInspectApiaryCommand {

    private static final int SEARCH_RADIUS = 12;

    private DebugInspectApiaryCommand() {}

    static int execute(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();

        Optional<GeneticApiaryBlockEntity> nearestApiary = findNearestApiary(source);
        if (nearestApiary.isEmpty()) {
            source.sendFailure(Component.literal(
                    "No Genetic Apiary found within " + SEARCH_RADIUS + " blocks."));
            return 0;
        }

        GeneticApiaryBlockEntity apiary = nearestApiary.get();
        ItemStackHandler output = apiary.outputInventory();

        source.sendSuccess(() -> Component.literal(
                "=== Genetic Apiary @ " + apiary.getBlockPos().toShortString() + " ==="), false);

        boolean anyItems = false;
        for (int i = 0; i < output.getSlots(); i++) {
            final int slot = i;
            var stack = output.getStackInSlot(i);
            if (stack.isEmpty()) {
                source.sendSuccess(() -> Component.literal("Slot " + slot + ": [empty]"), false);
            } else {
                anyItems = true;
                String itemId = stack.getItemHolder().unwrapKey()
                        .map(key -> key.location().toString())
                        .orElse(stack.getItem().toString());
                int count = stack.getCount();
                source.sendSuccess(() -> Component.literal(
                        "Slot " + slot + ": " + itemId + " x" + count), false);
            }
        }
        if (!anyItems) {
            source.sendSuccess(() -> Component.literal("Output inventory is empty."), false);
        }
        return 1;
    }

    private static Optional<GeneticApiaryBlockEntity> findNearestApiary(CommandSourceStack source) {
        Vec3 pos = source.getPosition();
        BlockPos center = BlockPos.containing(pos);

        GeneticApiaryBlockEntity best = null;
        double bestDistance = Double.MAX_VALUE;

        for (BlockPos candidate : BlockPos.betweenClosed(
                center.offset(-SEARCH_RADIUS, -SEARCH_RADIUS, -SEARCH_RADIUS),
                center.offset(SEARCH_RADIUS, SEARCH_RADIUS, SEARCH_RADIUS))) {
            if (!(source.getLevel().getBlockEntity(candidate) instanceof GeneticApiaryBlockEntity apiary)) {
                continue;
            }
            double dist = candidate.distSqr(center);
            if (dist < bestDistance) {
                best = apiary;
                bestDistance = dist;
            }
        }
        return Optional.ofNullable(best);
    }
}
