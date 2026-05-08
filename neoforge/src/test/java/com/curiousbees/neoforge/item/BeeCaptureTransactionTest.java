package com.curiousbees.neoforge.item;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Contract tests for the bee-capture transaction in CapturedBeeItem#interactLivingEntity.
 *
 * WHY THIS FILE IS HERE (not in common/src/test):
 * Capture logic is implemented in CapturedBeeItem which extends net.minecraft.world.item.Item
 * and uses net.minecraft.world.entity.animal.Bee, ItemStack, Player, Level, etc.
 * All of these require a game bootstrap to instantiate. Tests live in neoforge/src/test.
 *
 * IMPLEMENTATION NOTE (PR-T01, ADR-0014):
 * The capture transaction is atomic. The logic is:
 *   1. Guard: item already loaded → PASS (no state change)
 *   2. Guard: bee has no genome → PASS (only genomic bees are capturable)
 *   3. Build CapturedBeeData from genome + analysis flag
 *   4. Set component on stack — wrapped in try/catch; on failure return FAIL
 *   5. bee.discard() — ONLY called after step 4 succeeds; bee is never lost on serialization error
 *
 * TOOLTIP CONTRACT (PR-T01, ADR-0016):
 * appendHoverText always deserializes the genome and shows species + productivity + flower_type.
 * There is NO analysis gate on tooltip. analyzed flag is ignored for display purposes.
 *
 * @see com.curiousbees.neoforge.item.CapturedBeeItem#interactLivingEntity
 */
@Disabled("Awaiting PR-T02: neoforge/src/test bootstrap infrastructure not yet wired — " +
          "CapturedBeeItem uses Minecraft entity/item API that requires game bootstrap")
class BeeCaptureTransactionTest {

    /**
     * Capture removes the bee entity only AFTER the item component has been set.
     * The bee must still be alive if the data assignment fails before bee.discard().
     */
    @Test
    void captureOnlyDiscardsBeeAfterItemIsLoaded() {
        // TODO: create mock Bee with a genome attachment
        // TODO: call interactLivingEntity with an empty item stack
        // TODO: assert: stack has CAPTURED_BEE component AND bee.isRemoved() == true
        // TODO: assert ordering: component set before discard (verify via spy/sequence)
    }

    /**
     * If setting the data component throws (simulated failure), the bee must still be alive.
     * No state must have been mutated on the bee.
     */
    @Test
    void captureFailureLeavesBeeAlive() {
        // TODO: inject a failing ModDataComponents.CAPTURED_BEE stub
        // TODO: assert bee.isRemoved() == false after the failed capture attempt
    }

    /**
     * Attempting to capture a bee with an already-loaded item returns PASS and does nothing.
     */
    @Test
    void captureFailsIfItemAlreadyLoaded() {
        // TODO: build an ItemStack that already has CAPTURED_BEE component set
        // TODO: call interactLivingEntity → assert InteractionResult.PASS
        // TODO: assert bee is still alive and component is unchanged
    }

    /**
     * Attempting to capture a non-bee entity returns PASS and does nothing.
     */
    @Test
    void capturePassesForNonBeeEntity() {
        // TODO: call interactLivingEntity with a Cow entity
        // TODO: assert InteractionResult.PASS, no state change
    }
}
