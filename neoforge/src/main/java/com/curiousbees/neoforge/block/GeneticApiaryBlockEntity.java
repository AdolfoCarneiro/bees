package com.curiousbees.neoforge.block;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.common.content.frames.BuiltinFrameModifiers;
import com.curiousbees.common.gameplay.frames.FrameModifier;
import com.curiousbees.common.gameplay.frames.FrameModifiers;
import com.curiousbees.common.gameplay.production.ProductionOutput;
import com.curiousbees.common.gameplay.production.ProductionResolver;
import com.curiousbees.common.gameplay.production.ProductionResult;
import com.curiousbees.common.gameplay.spawn.WildBeeSpawnService;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import com.curiousbees.neoforge.content.NeoForgeContentRegistry;
import com.curiousbees.neoforge.data.BeeAnalysisStorage;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import com.curiousbees.neoforge.registry.ModBlockEntities;
import com.curiousbees.neoforge.menu.GeneticApiaryMenu;
import com.curiousbees.neoforge.registry.ModItems;
import com.curiousbees.neoforge.registry.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

/**
 * Block entity for the Genetic Apiary.
 *
 * Extends BeehiveBlockEntity so vanilla bee AI (instanceof checks) recognizes
 * it as a valid hive. getType() is overridden so NBT serialization uses our
 * registered type (curiousbees:genetic_apiary) instead of minecraft:beehive.
 *
 * <p>Behavior matches a vanilla crafted {@link net.minecraft.world.level.block.BeehiveBlock}:
 * facing, honey level, shear/bottle harvest, and bee occupancy through inherited ticking.
 * Any bee species may enter (no species gate — unlike species bee nests).
 *
 * <p>When a bee carrying nectar enters, Curious Bees also runs a production roll into the
 * output inventory using the bee's genome and installed frames. Breeding remains vanilla.
 */
public final class GeneticApiaryBlockEntity extends BeehiveBlockEntity implements MenuProvider {

    public static final int OUTPUT_SLOTS = 6;
    public static final int FRAME_SLOTS = 3;

    private static final ProductionResolver PRODUCTION_RESOLVER = new ProductionResolver();

    private final Random random = new Random();
    private final ItemStackHandler frameInventory = new ItemStackHandler(FRAME_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return isFrameItem(stack);
        }
    };
    private final ItemStackHandler outputInventory = new ItemStackHandler(OUTPUT_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            // Output-only inventory. Future automation/manual UI extracts from here.
            return false;
        }
    };
    /**
     * Side access (non-DOWN faces): frames insertable in slots 0–2, outputs extract-only in slots 3–8.
     * Automation pipes on the side can insert frames; hoppers cannot extract from frame slots.
     */
    private final IItemHandler automationOutputView = new IItemHandler() {
        @Override
        public int getSlots() {
            return frameInventory.getSlots() + outputInventory.getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            if (slot < frameInventory.getSlots()) {
                return frameInventory.getStackInSlot(slot);
            }
            return outputInventory.getStackInSlot(slot - frameInventory.getSlots());
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot < frameInventory.getSlots()) {
                return frameInventory.insertItem(slot, stack, simulate);
            }
            return stack; // output slots are extract-only
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot < frameInventory.getSlots()) {
                return frameInventory.extractItem(slot, amount, simulate);
            }
            return outputInventory.extractItem(slot - frameInventory.getSlots(), amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            if (slot < frameInventory.getSlots()) {
                return frameInventory.getSlotLimit(slot);
            }
            return outputInventory.getSlotLimit(slot - frameInventory.getSlots());
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot < frameInventory.getSlots()) {
                return frameInventory.isItemValid(slot, stack);
            }
            return false;
        }
    };

    /**
     * DOWN-face access: output slots only, extract-only. Hoppers below the apiary pull combs
     * but cannot insert frames. Frame slots are invisible on this face.
     */
    private final IItemHandler outputExtractView = new IItemHandler() {
        @Override
        public int getSlots() {
            return outputInventory.getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return outputInventory.getStackInSlot(slot);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return stack; // extract-only
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return outputInventory.extractItem(slot, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return outputInventory.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return false; // extract-only
        }
    };

    private int cacheTicksRemaining = 0;
    private int cachedHomedBeeCount = 0;
    private int cachedAnalyzedBeeCount = 0;

    public GeneticApiaryBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    /**
     * Custom server tick: delegates to vanilla hive logic then refreshes the bee-count cache
     * every 20 ticks. Called by GeneticApiaryBlock.getTicker() instead of BeehiveBlockEntity::serverTick.
     */
    public static void serverTick(Level level, BlockPos pos, BlockState state,
                                   GeneticApiaryBlockEntity blockEntity) {
        BeehiveBlockEntity.serverTick(level, pos, state, blockEntity);
        if (blockEntity.cacheTicksRemaining-- <= 0) {
            blockEntity.refreshBeeCountCache();
            blockEntity.cacheTicksRemaining = 20;
        }
    }

    private void refreshBeeCountCache() {
        if (level == null || level.isClientSide()) return;
        AABB box = new AABB(getBlockPos()).inflate(48);
        int outside = (int) level.getEntitiesOfClass(Bee.class, box,
                bee -> getBlockPos().equals(bee.getHivePos())).size();
        cachedHomedBeeCount = getOccupantCount() + outside;
        cachedAnalyzedBeeCount = (int) level.getEntitiesOfClass(Bee.class, box,
                bee -> getBlockPos().equals(bee.getHivePos()) && BeeAnalysisStorage.isAnalyzed(bee)).size();
    }

    /**
     * Override so NBT saves "curiousbees:genetic_apiary" instead of "minecraft:beehive".
     * BeehiveBlockEntity hardcodes BlockEntityType.BEEHIVE in its constructor; overriding
     * this method is the correct way to redirect serialization without a mixin.
     */
    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.GENETIC_APIARY.get();
    }

    public ItemStackHandler outputInventory() {
        return outputInventory;
    }

    public ItemStackHandler frameInventory() {
        return frameInventory;
    }

    public IItemHandler automationOutputView() {
        return automationOutputView;
    }

    /** Extract-only view of output slots only. Exposed on the DOWN face to hoppers below. */
    public IItemHandler outputExtractView() {
        return outputExtractView;
    }

    public FrameModifiers.CombinedFrameModifier currentFrameModifiers() {
        return combinedFrameModifier();
    }

    /**
     * Returns the cached count of bees homed to this apiary (inside + outside).
     * Refreshed every 20 ticks via serverTick and immediately on addOccupant.
     */
    public int homedBeeCount() {
        return cachedHomedBeeCount;
    }

    /**
     * Returns the cached count of analyzed outside bees homed here.
     * Bees inside the hive are not counted (entity scan only covers outside bees).
     * Refreshed every 20 ticks via serverTick and immediately on addOccupant.
     */
    public int analyzedBeeCount() {
        return cachedAnalyzedBeeCount;
    }

    /** Collects species display names for outside bees homed here (analyzed only, max 3). */
    public List<String> analyzedBeeSpeciesLabels() {
        if (level == null || level.isClientSide()) return List.of();
        List<String> labels = new ArrayList<>();
        for (Bee bee : level.getEntitiesOfClass(
                Bee.class,
                new net.minecraft.world.phys.AABB(getBlockPos()).inflate(48),
                bee -> getBlockPos().equals(bee.getHivePos()) && BeeAnalysisStorage.isAnalyzed(bee))) {
            if (labels.size() >= 3) break;
            BeeGenomeStorage.getGenome(bee).ifPresent(genome -> {
                String activeId = genome.getActiveAllele(
                        com.curiousbees.common.genetics.model.ChromosomeType.SPECIES).id();
                int slash = activeId.lastIndexOf('/');
                String name = slash >= 0 ? activeId.substring(slash + 1) : activeId;
                labels.add(Character.toUpperCase(name.charAt(0)) + name.substring(1));
            });
        }
        return labels;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.curiousbees.genetic_apiary");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new GeneticApiaryMenu(containerId, playerInventory, this);
    }

    @Override
    public void addOccupant(Entity occupant) {
        boolean hadNectar = occupant instanceof Bee bee && bee.hasNectar();
        super.addOccupant(occupant);
        cacheTicksRemaining = 0; // refresh cache on next tick after a bee enters
        if (level == null || level.isClientSide()) {
            return;
        }
        if (!(occupant instanceof Bee bee)) {
            return;
        }
        if (!hadNectar) {
            return;
        }
        if (!hasAnyOutputSpace()) {
            CuriousBeesMod.LOGGER.debug(
                    "Apiary {} has no output space; skipping production roll for bee {}.",
                    getBlockPos(), bee.getUUID());
            return;
        }

        Optional<Genome> genome = resolveOrAssignGenome(bee);
        if (genome.isEmpty()) {
            return;
        }

        FrameModifiers.CombinedFrameModifier combinedFrameModifier = combinedFrameModifier();
        ProductionResult result = rollProduction(genome.get(), combinedFrameModifier.productionMultiplier());
        int inserted = insertProductionResult(result);
        if (inserted > 0) {
            CuriousBeesMod.LOGGER.debug(
                    "Apiary {} produced {} items from bee {} (species={}, frameMutationMultiplier={}, frameProductionMultiplier={}).",
                    getBlockPos(),
                    inserted,
                    bee.getUUID(),
                    result.activeSpeciesId(),
                    combinedFrameModifier.mutationMultiplier(),
                    combinedFrameModifier.productionMultiplier());
        }
    }

    /**
     * Runs one production roll from a genome using common production rules.
     * This method is platform orchestration only; production logic stays in common.
     */
    public ProductionResult rollProduction(Genome genome) {
        Objects.requireNonNull(genome, "genome must not be null");
        return rollProduction(genome, 1.0);
    }

    public ProductionResult rollProduction(Genome genome, double frameProductionMultiplier) {
        Objects.requireNonNull(genome, "genome must not be null");
        return PRODUCTION_RESOLVER.resolve(
                genome,
                NeoForgeContentRegistry.current().productionBySpeciesId(),
                new JavaGeneticRandom(random),
                frameProductionMultiplier);
    }

    /**
     * Converts generated outputs to ItemStacks and inserts them in the output inventory.
     *
     * @return total items inserted across all generated outputs.
     */
    public int insertProductionResult(ProductionResult result) {
        Objects.requireNonNull(result, "result must not be null");
        int insertedTotal = 0;
        for (ProductionOutput output : result.generatedOutputs()) {
            insertedTotal += insertOutput(output);
        }
        if (insertedTotal > 0) {
            setChanged();
        }
        return insertedTotal;
    }

    private int insertOutput(ProductionOutput output) {
        Optional<Item> maybeItem = resolveItem(output.outputId());
        if (maybeItem.isEmpty()) {
            CuriousBeesMod.LOGGER.warn("Unknown production output id '{}' for apiary at {}.",
                    output.outputId(), getBlockPos());
            return 0;
        }

        ItemStack remaining = new ItemStack(maybeItem.get(), output.count());
        int initialCount = remaining.getCount();

        for (int i = 0; i < outputInventory.getSlots() && !remaining.isEmpty(); i++) {
            remaining = outputInventory.insertItem(i, remaining, false);
        }
        int inserted = initialCount - remaining.getCount();
        if (!remaining.isEmpty()) {
            CuriousBeesMod.LOGGER.warn(
                    "Apiary {} output inventory full. Dropped {}x '{}' from production roll.",
                    getBlockPos(), remaining.getCount(), output.outputId());
        }
        return inserted;
    }

    private boolean hasAnyOutputSpace() {
        for (int i = 0; i < outputInventory.getSlots(); i++) {
            ItemStack stack = outputInventory.getStackInSlot(i);
            if (stack.isEmpty() || stack.getCount() < stack.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    private FrameModifiers.CombinedFrameModifier combinedFrameModifier() {
        java.util.ArrayList<FrameModifier> modifiers = new java.util.ArrayList<>();
        for (int i = 0; i < frameInventory.getSlots(); i++) {
            ItemStack stack = frameInventory.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }
            resolveFrameModifier(stack).ifPresent(modifiers::add);
        }
        return FrameModifiers.combine(modifiers);
    }

    private Optional<FrameModifier> resolveFrameModifier(ItemStack frameStack) {
        return frameStack.getItemHolder()
                .unwrapKey()
                .map(key -> key.location().toString())
                .map(BuiltinFrameModifiers.BY_ID::get);
    }

    private boolean isFrameItem(ItemStack stack) {
        return stack.is(ModTags.Items.FRAMES);
    }

    private Optional<Item> resolveItem(String outputId) {
        ResourceLocation key = ResourceLocation.tryParse(outputId);
        if (key == null) {
            CuriousBeesMod.LOGGER.warn("Invalid production output id '{}' for apiary at {}.",
                    outputId, getBlockPos());
            return Optional.empty();
        }

        if (!BuiltInRegistries.ITEM.containsKey(key)) {
            return Optional.empty();
        }
        Item item = BuiltInRegistries.ITEM.get(key);
        return Optional.of(item);
    }

    private Optional<Genome> resolveOrAssignGenome(Bee bee) {
        Optional<Genome> existing = BeeGenomeStorage.getGenome(bee);
        if (existing.isPresent()) {
            return existing;
        }

        String category = resolveBiomeCategory();
        Genome fallback = WildBeeSpawnService.createWildGenome(
                category,
                new JavaGeneticRandom(random));
        BeeGenomeStorage.setGenome(bee, fallback);

        CuriousBeesMod.LOGGER.warn(
                "Bee {} entered apiary {} without genome. Assigned fallback biome genome '{}'.",
                bee.getUUID(), getBlockPos(), category);
        return Optional.of(fallback);
    }

    private String resolveBiomeCategory() {
        if (level == null) {
            return WildBeeSpawnService.CATEGORY_MEADOW;
        }
        var biomeHolder = level.getBiome(getBlockPos());
        if (biomeHolder.is(BiomeTags.IS_FOREST)) {
            return WildBeeSpawnService.CATEGORY_FOREST;
        }
        if (biomeHolder.is(BiomeTags.IS_SAVANNA) || biomeHolder.is(BiomeTags.IS_BADLANDS)) {
            return WildBeeSpawnService.CATEGORY_ARID;
        }
        return WildBeeSpawnService.CATEGORY_MEADOW;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("FrameInventory", frameInventory.serializeNBT(registries));
        tag.put("OutputInventory", outputInventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("FrameInventory")) {
            frameInventory.deserializeNBT(registries, tag.getCompound("FrameInventory"));
        }
        if (tag.contains("OutputInventory")) {
            outputInventory.deserializeNBT(registries, tag.getCompound("OutputInventory"));
        }
    }
}
