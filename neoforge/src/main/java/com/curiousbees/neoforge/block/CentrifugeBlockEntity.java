package com.curiousbees.neoforge.block;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.neoforge.menu.CentrifugeMenu;
import com.curiousbees.neoforge.recipe.CentrifugeRecipe;
import com.curiousbees.neoforge.registry.ModBlockEntities;
import com.curiousbees.neoforge.registry.ModRecipes;
import com.curiousbees.neoforge.registry.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Optional;
import java.util.Random;

/**
 * Block entity for the Centrifuge (E4).
 *
 * <p>Processes combs into item outputs + honey (ADR-0015).
 * Honey output is tracked as a soft buffer (honeyCounter 0-5) and bottled
 * when a glass bottle is present; overflow is discarded at FINE — never
 * blocks processing.
 */
public final class CentrifugeBlockEntity extends BlockEntity implements MenuProvider {

    public static final int INPUT_SLOTS  = 1;
    public static final int BOTTLE_SLOTS = 1;
    public static final int OUTPUT_SLOTS = 4;

    private final Random random = new Random();

    private final ItemStackHandler inputInventory = new ItemStackHandler(INPUT_SLOTS) {
        @Override protected void onContentsChanged(int slot) { setChanged(); }
    };

    private final ItemStackHandler bottleInventory = new ItemStackHandler(BOTTLE_SLOTS) {
        @Override protected void onContentsChanged(int slot) { setChanged(); }
    };

    private final ItemStackHandler outputInventory = new ItemStackHandler(OUTPUT_SLOTS) {
        @Override protected void onContentsChanged(int slot) { setChanged(); }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return false; // output-only
        }
    };

    /**
     * Automation view: input (insert) | bottle (insert) | outputs (extract-only).
     * Slot indices: 0 = comb input, 1 = bottle input, 2-5 = outputs.
     */
    private final IItemHandler automationView = new IItemHandler() {
        @Override public int getSlots() { return INPUT_SLOTS + BOTTLE_SLOTS + OUTPUT_SLOTS; }

        @Override
        public ItemStack getStackInSlot(int slot) {
            if (slot == 0) return inputInventory.getStackInSlot(0);
            if (slot == 1) return bottleInventory.getStackInSlot(0);
            return outputInventory.getStackInSlot(slot - 2);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot == 0) return inputInventory.insertItem(0, stack, simulate);
            if (slot == 1) return bottleInventory.insertItem(0, stack, simulate);
            return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot < 2) return ItemStack.EMPTY;
            return outputInventory.extractItem(slot - 2, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            if (slot == 0) return inputInventory.getSlotLimit(0);
            if (slot == 1) return bottleInventory.getSlotLimit(0);
            return outputInventory.getSlotLimit(slot - 2);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == 0) return inputInventory.isItemValid(0, stack);
            if (slot == 1) return bottleInventory.isItemValid(0, stack);
            return false;
        }
    };

    /** Honey portions buffered (0-5). Full counter never pauses processing (ADR-0015). */
    private int honeyCounter = 0;

    /** Progress synced to client for GUI progress bar. */
    private int processingProgress = 0;
    private int processingTotal    = 0;

    public CentrifugeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CENTRIFUGE.get(), pos, state);
    }

    // --- Server tick ---

    public static void serverTick(Level level, BlockPos pos, BlockState state,
                                   CentrifugeBlockEntity be) {
        be.tryBottle();
        be.tickProcessing(level);
    }

    private void tickProcessing(Level level) {
        ItemStack inputStack = inputInventory.getStackInSlot(0);
        if (inputStack.isEmpty()) {
            resetProgress();
            return;
        }

        Optional<RecipeHolder<CentrifugeRecipe>> maybeHolder =
                level.getRecipeManager().getRecipeFor(
                        ModRecipes.CENTRIFUGE_TYPE.get(),
                        new SingleRecipeInput(inputStack),
                        level);

        if (maybeHolder.isEmpty()) {
            resetProgress();
            return;
        }

        CentrifugeRecipe recipe = maybeHolder.get().value();

        if (processingTotal != recipe.processingTime()) {
            processingTotal    = recipe.processingTime();
            processingProgress = 0;
        }

        // Pause if no room for any output (don't waste combs)
        if (!hasAnyOutputSpace()) {
            return;
        }

        processingProgress++;
        setChanged();

        if (processingProgress >= processingTotal) {
            processBatch(recipe);
            processingProgress = 0;
        }
    }

    private void processBatch(CentrifugeRecipe recipe) {
        // Consume input
        inputInventory.extractItem(0, recipe.inputCount(), false);

        // Roll item outputs
        for (CentrifugeRecipe.WeightedOutput out : recipe.outputs()) {
            if (out.chance() >= 1.0f || random.nextFloat() < out.chance()) {
                insertIntoOutput(out.stack().copy());
            }
        }

        // Honey counter (ADR-0015: overflow discarded at FINE, never blocks)
        if (recipe.honeyPortions() > 0) {
            int before = honeyCounter;
            honeyCounter = Math.min(5, honeyCounter + recipe.honeyPortions());
            int overflow = recipe.honeyPortions() - (honeyCounter - before);
            if (overflow > 0) {
                CuriousBeesMod.LOGGER.debug(
                        "Centrifuge at {}: honey counter full, discarding {} portion(s).",
                        getBlockPos(), overflow);
            }
        }
        if (level != null && !level.isClientSide()) {
            level.playSound(null, getBlockPos(), ModSounds.CENTRIFUGE_WORK.get(),
                    SoundSource.BLOCKS, 0.8f, 0.85f + level.getRandom().nextFloat() * 0.3f);
        }
        setChanged();
    }

    /**
     * Tries to convert one honey counter portion to a honey bottle if a glass
     * bottle is available and output space exists.
     */
    private void tryBottle() {
        if (honeyCounter <= 0) return;

        ItemStack bottle = bottleInventory.getStackInSlot(0);
        if (bottle.isEmpty() || !bottle.is(Items.GLASS_BOTTLE)) return;

        ItemStack honeyBottle = new ItemStack(Items.HONEY_BOTTLE);
        ItemStack remaining = insertIntoOutput(honeyBottle);
        if (!remaining.isEmpty()) return; // no output space

        bottleInventory.extractItem(0, 1, false);
        honeyCounter--;
        setChanged();
    }

    private ItemStack insertIntoOutput(ItemStack stack) {
        ItemStack remaining = stack.copy();
        for (int i = 0; i < outputInventory.getSlots() && !remaining.isEmpty(); i++) {
            remaining = outputInventory.insertItem(i, remaining, false);
        }
        return remaining;
    }

    private boolean hasAnyOutputSpace() {
        for (int i = 0; i < outputInventory.getSlots(); i++) {
            ItemStack s = outputInventory.getStackInSlot(i);
            if (s.isEmpty() || s.getCount() < s.getMaxStackSize()) return true;
        }
        return false;
    }

    private void resetProgress() {
        if (processingProgress != 0 || processingTotal != 0) {
            processingProgress = 0;
            processingTotal    = 0;
            setChanged();
        }
    }

    // --- Accessors ---

    public ItemStackHandler inputInventory()  { return inputInventory; }
    public ItemStackHandler bottleInventory() { return bottleInventory; }
    public ItemStackHandler outputInventory() { return outputInventory; }
    public IItemHandler     automationView()  { return automationView; }
    public int honeyCounter()                 { return honeyCounter; }
    public int processingProgress()           { return processingProgress; }
    public int processingTotal()              { return processingTotal; }

    // --- MenuProvider ---

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.curiousbees.centrifuge");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new CentrifugeMenu(containerId, playerInventory, this);
    }

    // --- NBT ---

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("InputInventory",  inputInventory.serializeNBT(registries));
        tag.put("BottleInventory", bottleInventory.serializeNBT(registries));
        tag.put("OutputInventory", outputInventory.serializeNBT(registries));
        tag.putInt("HoneyCounter",       honeyCounter);
        tag.putInt("ProcessingProgress", processingProgress);
        tag.putInt("ProcessingTotal",    processingTotal);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("InputInventory"))
            inputInventory.deserializeNBT(registries, tag.getCompound("InputInventory"));
        if (tag.contains("BottleInventory"))
            bottleInventory.deserializeNBT(registries, tag.getCompound("BottleInventory"));
        if (tag.contains("OutputInventory"))
            outputInventory.deserializeNBT(registries, tag.getCompound("OutputInventory"));
        honeyCounter       = tag.getInt("HoneyCounter");
        processingProgress = tag.getInt("ProcessingProgress");
        processingTotal    = tag.getInt("ProcessingTotal");
    }
}
