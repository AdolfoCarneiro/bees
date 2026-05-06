package com.curiousbees.neoforge.client;

import com.curiousbees.neoforge.block.GeneticApiaryBlockEntity;
import com.curiousbees.neoforge.content.NeoForgeContentRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;

/**
 * Adds a Curious Bees telemetry block to the F3 debug screen.
 * Visible only in creative/spectator — not a gameplay information leak.
 *
 * Lines added to the right column:
 *   [CuriousBees] Species: N  Mutations: N
 *   Apiary: N bees inside, honey N/5   (only when crosshair targets an apiary)
 */
public final class ContentDebugOverlay {

    private ContentDebugOverlay() {}

    @SubscribeEvent
    public static void onDebugInfo(CustomizeGuiOverlayEvent.DebugText event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        if (!mc.player.isCreative() && !mc.player.isSpectator()) return;

        var registry = NeoForgeContentRegistry.current();
        event.getRight().add(String.format("[CuriousBees] Species: %d  Mutations: %d",
                registry.allSpecies().size(),
                registry.allMutations().size()));

        appendApiaryInfo(event, mc);
    }

    private static void appendApiaryInfo(CustomizeGuiOverlayEvent.DebugText event, Minecraft mc) {
        if (!(mc.hitResult instanceof BlockHitResult blockHit)) return;
        if (blockHit.getType() == HitResult.Type.MISS) return;

        BlockPos pos = blockHit.getBlockPos();
        BlockEntity be = mc.level.getBlockEntity(pos);
        if (!(be instanceof GeneticApiaryBlockEntity apiary)) return;

        int bees  = apiary.getOccupantCount();
        BlockState state = mc.level.getBlockState(pos);
        int honey = state.hasProperty(BeehiveBlock.HONEY_LEVEL)
                ? state.getValue(BeehiveBlock.HONEY_LEVEL) : 0;
        event.getRight().add(String.format("Apiary: %d bees inside, honey %d/5", bees, honey));
    }
}
