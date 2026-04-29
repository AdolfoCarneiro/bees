package com.curiousbees.neoforge.block;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.common.content.products.BuiltinProductionDefinitions;
import com.curiousbees.common.gameplay.production.ProductionOutput;
import com.curiousbees.common.gameplay.production.ProductionResolver;
import com.curiousbees.common.gameplay.production.ProductionResult;
import com.curiousbees.common.gameplay.spawn.WildBeeSpawnService;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import com.curiousbees.neoforge.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

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
 * Production logic (intercepting bee-enters-with-nectar) is added in Phase 7G.
 */
public final class GeneticApiaryBlockEntity extends BeehiveBlockEntity {

    public static final int OUTPUT_SLOTS = 6;

    private static final ProductionResolver PRODUCTION_RESOLVER = new ProductionResolver();

    private final Random random = new Random();
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
    private final IItemHandler automationOutputView = new IItemHandler() {
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
            return stack;
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
            return false;
        }
    };

    public GeneticApiaryBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
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

    public IItemHandler automationOutputView() {
        return automationOutputView;
    }

    @Override
    public void addOccupant(Entity occupant) {
        super.addOccupant(occupant);
        if (level == null || level.isClientSide()) {
            return;
        }
        if (!(occupant instanceof Bee bee)) {
            return;
        }
        if (!bee.hasNectar()) {
            return;
        }

        Optional<Genome> genome = resolveOrAssignGenome(bee);
        if (genome.isEmpty()) {
            return;
        }

        ProductionResult result = rollProduction(genome.get());
        int inserted = insertProductionResult(result);
        if (inserted > 0) {
            CuriousBeesMod.LOGGER.debug(
                    "Apiary {} produced {} items from bee {} (species={}).",
                    getBlockPos(), inserted, bee.getUUID(), result.activeSpeciesId());
        }
    }

    /**
     * Runs one production roll from a genome using common production rules.
     * This method is platform orchestration only; production logic stays in common.
     */
    public ProductionResult rollProduction(Genome genome) {
        Objects.requireNonNull(genome, "genome must not be null");
        return PRODUCTION_RESOLVER.resolve(
                genome,
                BuiltinProductionDefinitions.BY_SPECIES_ID,
                new JavaGeneticRandom(random));
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
        return initialCount - remaining.getCount();
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
        tag.put("OutputInventory", outputInventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("OutputInventory")) {
            outputInventory.deserializeNBT(registries, tag.getCompound("OutputInventory"));
        }
    }
}
