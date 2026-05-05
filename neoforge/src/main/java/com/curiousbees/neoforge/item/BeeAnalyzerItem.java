package com.curiousbees.neoforge.item;

import com.curiousbees.common.gameplay.analysis.BeeAnalysisReport;
import com.curiousbees.common.gameplay.analysis.BeeAnalysisService;
import com.curiousbees.neoforge.data.BeeAnalysisStorage;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import com.curiousbees.neoforge.network.CuriousBeesNetwork;
import com.curiousbees.neoforge.network.ShowAnalyzerReportPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

import java.util.Optional;

/**
 * Right-click on a living Bee to analyze it.
 *
 * <p>Cost: 1 honeycomb from the player's inventory (consumed on first analysis only).
 * Re-analyzing an already-analyzed bee is free and opens the report again.
 *
 * <p>Flow: server validates → marks bee analyzed → generates AnalyzerReport
 * → sends ShowAnalyzerReportPayload → client opens BeeAnalyzerScreen.
 */
public final class BeeAnalyzerItem extends Item {

    private static final BeeAnalysisService ANALYSIS_SERVICE = new BeeAnalysisService();

    public BeeAnalyzerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player,
                                                   LivingEntity entity, InteractionHand hand) {
        if (!(entity instanceof Bee bee)) return InteractionResult.PASS;
        if (player.level().isClientSide()) return InteractionResult.SUCCESS;

        Optional<com.curiousbees.common.genetics.model.Genome> genome = BeeGenomeStorage.getGenome(bee);
        if (genome.isEmpty()) {
            player.sendSystemMessage(Component.translatable("item.curiousbees.bee_analyzer.no_genome"));
            return InteractionResult.SUCCESS;
        }

        boolean alreadyAnalyzed = BeeAnalysisStorage.isAnalyzed(bee);

        if (!alreadyAnalyzed) {
            // Require 1 honeycomb to perform first analysis
            if (!consumeHoneycomb(player)) {
                player.sendSystemMessage(Component.translatable("item.curiousbees.bee_analyzer.no_honeycomb"));
                return InteractionResult.FAIL;
            }
            BeeAnalysisStorage.setAnalyzed(bee);
            CuriousBeesNetwork.syncAnalyzedToTracking(bee);
        }

        BeeAnalysisReport report = ANALYSIS_SERVICE.analyze(genome.get());

        if (player instanceof ServerPlayer serverPlayer) {
            CuriousBeesNetwork.sendAnalyzerReport(serverPlayer, report);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.curiousbees.bee_analyzer.tooltip.use")
                .withStyle(net.minecraft.ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.curiousbees.bee_analyzer.tooltip.cost")
                .withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
    }

    /** Removes 1 honeycomb from the player's inventory. Returns true if successful. */
    private static boolean consumeHoneycomb(Player player) {
        if (player.isCreative()) return true;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack slot = player.getInventory().getItem(i);
            if (slot.is(Items.HONEYCOMB)) {
                slot.shrink(1);
                return true;
            }
        }
        return false;
    }
}
