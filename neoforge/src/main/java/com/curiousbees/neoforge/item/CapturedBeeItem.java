package com.curiousbees.neoforge.item;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.content.visual.SpeciesVisualDefinition;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.serial.GenomeSerializer;
import com.curiousbees.neoforge.content.NeoForgeContentRegistry;
import com.curiousbees.neoforge.data.BeeAnalysisStorage;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import com.curiousbees.neoforge.data.CapturedBeeData;
import com.curiousbees.neoforge.registry.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public abstract class CapturedBeeItem extends Item {

    protected CapturedBeeItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    /** Capture a bee by right-clicking it. Requires the item to be empty. */
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player,
                                                   LivingEntity entity, InteractionHand hand) {
        if (!(entity instanceof Bee bee)) return InteractionResult.PASS;
        if (stack.has(ModDataComponents.CAPTURED_BEE.get())) return InteractionResult.PASS;
        if (player.level().isClientSide()) return InteractionResult.SUCCESS;

        var genomeOpt = BeeGenomeStorage.getGenome(bee);
        if (genomeOpt.isEmpty()) {
            CuriousBeesMod.LOGGER.warn("CapturedBeeItem [DIAG]: bee {} has no genome — not capturing (PASS)", bee.getUUID());
            return InteractionResult.PASS;
        }

        CuriousBeesMod.LOGGER.warn("CapturedBeeItem [DIAG]: capturing bee {}, setting component", bee.getUUID());
        CapturedBeeData data = new CapturedBeeData(
                GenomeSerializer.toData(genomeOpt.get()),
                BeeAnalysisStorage.isAnalyzed(bee));
        try {
            stack.set(ModDataComponents.CAPTURED_BEE.get(), data);
        } catch (Exception e) {
            CuriousBeesMod.LOGGER.warn("CapturedBeeItem: failed to serialize bee data — bee not discarded. {}", e.getMessage());
            return InteractionResult.FAIL;
        }
        CuriousBeesMod.LOGGER.warn("CapturedBeeItem [DIAG]: bee {} discarding, component in stack: {}", bee.getUUID(), stack.has(ModDataComponents.CAPTURED_BEE.get()));
        bee.discard();
        return InteractionResult.SUCCESS;
    }

    /** Release the stored bee at the player's position. */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.has(ModDataComponents.CAPTURED_BEE.get())) {
            return InteractionResultHolder.pass(stack);
        }
        if (level.isClientSide()) {
            return InteractionResultHolder.success(stack);
        }

        CapturedBeeData data = stack.get(ModDataComponents.CAPTURED_BEE.get());
        Vec3 pos = player.position().add(0, 0.5, 0);
        Bee bee = new Bee(EntityType.BEE, (ServerLevel) level);
        bee.setPos(pos.x, pos.y, pos.z);

        GenomeSerializer.fromData(data.genome(), NeoForgeContentRegistry.current()::findAllele)
                .ifPresent(genome -> BeeGenomeStorage.setGenome(bee, genome));
        if (data.analyzed()) {
            BeeAnalysisStorage.setAnalyzed(bee);
        }

        if (!level.addFreshEntity(bee)) {
            CuriousBeesMod.LOGGER.warn("CapturedBeeItem: failed to spawn bee at {} — item unchanged.", pos);
            return InteractionResultHolder.fail(stack);
        }
        return InteractionResultHolder.success(releaseItem(stack));
    }

    /**
     * Returns the item stack after releasing the bee.
     * BeeJar: shrink to empty. BeeTransporter: remove component.
     */
    protected abstract ItemStack releaseItem(ItemStack stack);

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                 List<Component> tooltip, TooltipFlag flag) {
        CapturedBeeData data = stack.get(ModDataComponents.CAPTURED_BEE.get());
        if (data == null) {
            tooltip.add(Component.translatable("item.curiousbees.captured_bee.empty")
                    .withStyle(ChatFormatting.GRAY));
            return;
        }

        var genomeOpt = GenomeSerializer
                .fromData(data.genome(), NeoForgeContentRegistry.current()::findAllele);

        if (genomeOpt.isEmpty()) {
            tooltip.add(Component.translatable("item.curiousbees.captured_bee.genome_corrupt")
                    .withStyle(ChatFormatting.RED));
            return;
        }

        var genome = genomeOpt.get();

        // Species
        String speciesId = genome.getActiveAllele(ChromosomeType.SPECIES).id();
        String speciesLabel = NeoForgeContentRegistry.current().findSpecies(speciesId)
                .flatMap(BeeSpeciesDefinition::visualDefinition)
                .flatMap(SpeciesVisualDefinition::displayNameKey)
                .map(key -> Component.translatable(key).getString())
                .orElse(speciesId);
        tooltip.add(Component.translatable("item.curiousbees.captured_bee.species", speciesLabel)
                .withStyle(ChatFormatting.YELLOW));

        // Productivity
        String productivityId = genome.getActiveAllele(ChromosomeType.PRODUCTIVITY).id();
        String productivityLabel = formatAlleleId(productivityId);
        tooltip.add(Component.translatable("item.curiousbees.captured_bee.productivity", productivityLabel)
                .withStyle(ChatFormatting.GRAY));

        // Flower type
        String flowerTypeId = genome.getActiveAllele(ChromosomeType.FLOWER_TYPE).id();
        String flowerTypeLabel = formatAlleleId(flowerTypeId);
        tooltip.add(Component.translatable("item.curiousbees.captured_bee.flower_type", flowerTypeLabel)
                .withStyle(ChatFormatting.GRAY));
    }

    /**
     * Converts an allele ID like "curious_bees:productivity/normal" to "Normal"
     * by taking the last path segment and capitalizing the first letter.
     */
    private static String formatAlleleId(String alleleId) {
        if (alleleId == null || alleleId.isEmpty()) return "?";
        String path = alleleId.contains("/") ? alleleId.substring(alleleId.lastIndexOf('/') + 1) : alleleId;
        if (path.isEmpty()) return alleleId;
        return Character.toUpperCase(path.charAt(0)) + path.substring(1).replace('_', ' ');
    }
}
