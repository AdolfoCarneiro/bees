package com.curiousbees.neoforge.item;

import com.curiousbees.common.gameplay.analysis.BeeAnalysisFormatter;
import com.curiousbees.common.gameplay.analysis.BeeAnalysisReport;
import com.curiousbees.common.gameplay.analysis.BeeAnalysisService;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

/**
 * Right-click on a Bee to read its genome and display the report in chat.
 * Inspect-only: does not modify genome data.
 */
public final class BeeAnalyzerItem extends Item {

    private static final BeeAnalysisService ANALYSIS_SERVICE = new BeeAnalysisService();

    public BeeAnalyzerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player,
                                                   LivingEntity entity, InteractionHand hand) {
        if (!(entity instanceof Bee bee)) {
            return InteractionResult.PASS;
        }
        if (player.level().isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        Optional<com.curiousbees.common.genetics.model.Genome> genome = BeeGenomeStorage.getGenome(bee);
        if (genome.isEmpty()) {
            player.sendSystemMessage(Component.literal("This bee has no Curious Bees genome data yet."));
            return InteractionResult.SUCCESS;
        }

        BeeAnalysisReport report = ANALYSIS_SERVICE.analyze(genome.get());
        List<String> lines = BeeAnalysisFormatter.format(report);
        for (String line : lines) {
            player.sendSystemMessage(Component.literal(line));
        }

        return InteractionResult.SUCCESS;
    }
}
