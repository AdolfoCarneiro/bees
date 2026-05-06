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
        if (genomeOpt.isEmpty()) return InteractionResult.PASS; // only capture bees with a genome

        CapturedBeeData data = new CapturedBeeData(
                GenomeSerializer.toData(genomeOpt.get()),
                BeeAnalysisStorage.isAnalyzed(bee));
        stack.set(ModDataComponents.CAPTURED_BEE.get(), data);
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
        if (data.analyzed()) {
            String label = GenomeSerializer
                    .fromData(data.genome(), NeoForgeContentRegistry.current()::findAllele)
                    .map(g -> {
                        String sid = g.getActiveAllele(ChromosomeType.SPECIES).id();
                        return NeoForgeContentRegistry.current().findSpecies(sid)
                                .flatMap(BeeSpeciesDefinition::visualDefinition)
                                .flatMap(SpeciesVisualDefinition::displayNameKey)
                                .map(key -> Component.translatable(key).getString())
                                .orElse(sid);
                    }).orElse("?");
            tooltip.add(Component.translatable("item.curiousbees.captured_bee.species", label)
                    .withStyle(ChatFormatting.YELLOW));
            tooltip.add(Component.translatable("item.curiousbees.captured_bee.analyzed")
                    .withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(Component.translatable("item.curiousbees.captured_bee.species_unknown")
                    .withStyle(ChatFormatting.YELLOW));
            tooltip.add(Component.translatable("item.curiousbees.captured_bee.unanalyzed")
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}
