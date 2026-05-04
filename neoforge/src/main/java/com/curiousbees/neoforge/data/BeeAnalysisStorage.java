package com.curiousbees.neoforge.data;

import net.minecraft.world.entity.animal.Bee;

/** Read/write helpers for the per-bee analysis state attachment. */
public final class BeeAnalysisStorage {

    private BeeAnalysisStorage() {}

    /** Returns true if this bee has been analyzed by the portable analyzer. */
    public static boolean isAnalyzed(Bee bee) {
        return bee.getData(BeeAnalysisAttachments.ANALYZED);
    }

    /** Marks the bee as analyzed. Idempotent — safe to call multiple times. */
    public static void setAnalyzed(Bee bee) {
        bee.setData(BeeAnalysisAttachments.ANALYZED, true);
    }
}
