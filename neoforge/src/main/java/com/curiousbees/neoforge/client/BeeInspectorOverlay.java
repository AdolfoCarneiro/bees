package com.curiousbees.neoforge.client;

import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.neoforge.data.BeeAnalysisAttachments;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.animal.Bee;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;

import java.util.Optional;

/**
 * Adds a bee-species line to the F3 debug screen when the crosshair targets
 * a Bee with a genome. Restricted to creative/spectator so it does not
 * accidentally expose data in survival play.
 *
 * <p>Format: {@code Bee: curiousbees:species/meadow  [analyzed]}
 */
public final class BeeInspectorOverlay {

    private BeeInspectorOverlay() {}

    @SubscribeEvent
    public static void onDebugInfo(CustomizeGuiOverlayEvent.DebugInfo event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        if (!mc.player.isCreative() && !mc.player.isSpectator()) return;
        if (!(mc.crosshairPickEntity instanceof Bee bee)) return;

        Optional<Genome> genome = BeeGenomeStorage.getGenome(bee);
        if (genome.isEmpty()) return;

        String speciesId = genome.get().getActiveAllele(ChromosomeType.SPECIES).id();
        boolean analyzed = bee.getData(BeeAnalysisAttachments.ANALYZED.get());
        String flag = analyzed ? "[analyzed]" : "[unanalyzed]";

        event.getRight().add("Bee: " + speciesId + "  " + flag);
    }
}
