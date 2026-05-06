package com.curiousbees.neoforge.block;

import com.curiousbees.neoforge.menu.CentrifugeMenu;
import com.curiousbees.neoforge.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * Block entity for the Centrifuge (E4).
 *
 * <p>Processes combs into wax + honey (ADR-0015). Honey output is tracked as
 * a soft buffer (honeyCounter 0-5) and bottled when a glass bottle is present;
 * overflow is discarded at FINE log level — never blocks processing.
 *
 * <p>Recipe execution wired in E4-T04. This entity just maintains the inventory
 * and honey counter with correct persistence.
 */
public final class CentrifugeBlockEntity extends BlockEntity implements MenuProvider {

    public static final int INPUT_SLOTS  = 1;
    public static final int BOTTLE_SLOTS = 1;
    public static final int OUTPUT_SLOTS = 4;

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
            return stack; // output slots reject insert
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot < 2) return ItemStack.EMPTY; // input/bottle: no automation extract
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

    /** Processing progress ticks synced to client for the GUI progress bar (E4-T04). */
    private int processingProgress = 0;
    private int processingTotal    = 0;

    public CentrifugeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CENTRIFUGE.get(), pos, state);
    }

    /**
     * Server tick stub. Recipe processing logic added in E4-T04 once the recipe type exists.
     */
    public static void serverTick(Level level, BlockPos pos, BlockState state,
                                   CentrifugeBlockEntity be) {
        // Intentionally empty until E4-T04 wires in recipe execution.
    }

    // --- Accessors ---

    public ItemStackHandler inputInventory()  { return inputInventory; }
    public ItemStackHandler bottleInventory() { return bottleInventory; }
    public ItemStackHandler outputInventory() { return outputInventory; }
    public IItemHandler     automationView()  { return automationView; }
    public int honeyCounter()                 { return honeyCounter; }
    public int processingProgress()           { return processingProgress; }
    public int processingTotal()              { return processingTotal; }

    /** Called from E4-T04 processing logic to update honey counter and trigger bottling. */
    public void addHoney(int portions) {
        honeyCounter = Math.min(5, honeyCounter + portions);
        setChanged();
    }

    /** Called from E4-T04 processing logic to consume one honey portion for bottling. */
    public boolean consumeHoney() {
        if (honeyCounter <= 0) return false;
        honeyCounter--;
        setChanged();
        return true;
    }

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
        tag.putInt("HoneyCounter",        honeyCounter);
        tag.putInt("ProcessingProgress",  processingProgress);
        tag.putInt("ProcessingTotal",     processingTotal);
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
