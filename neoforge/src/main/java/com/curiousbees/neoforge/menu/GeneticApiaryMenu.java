package com.curiousbees.neoforge.menu;

import com.curiousbees.neoforge.block.GeneticApiaryBlockEntity;
import com.curiousbees.neoforge.registry.ModBlocks;
import com.curiousbees.neoforge.registry.ModMenuTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.Objects;

public final class GeneticApiaryMenu extends AbstractContainerMenu {

    private static final int FRAME_SLOTS = GeneticApiaryBlockEntity.FRAME_SLOTS;
    private static final int OUTPUT_SLOTS = GeneticApiaryBlockEntity.OUTPUT_SLOTS;

    /** Apiary slots (frames + output). */
    private static final int SLOT_COUNT = FRAME_SLOTS + OUTPUT_SLOTS;

    private final GeneticApiaryBlockEntity blockEntity;
    private final ContainerLevelAccess levelAccess;
    private final ContainerData syncData;

    public GeneticApiaryMenu(int containerId, Inventory playerInventory, GeneticApiaryBlockEntity blockEntity) {
        super(ModMenuTypes.GENETIC_APIARY.get(), containerId);
        this.blockEntity = Objects.requireNonNull(blockEntity, "blockEntity");
        this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        // Frames — top row
        for (int col = 0; col < FRAME_SLOTS; col++) {
            addSlot(new SlotItemHandler(blockEntity.frameInventory(), col, 62 + col * 18, 17));
        }

        // Outputs — two rows under frames
        int outputStartY = 53;
        for (int i = 0; i < OUTPUT_SLOTS; i++) {
            int col = i % 3;
            int row = i / 3;
            addSlot(new SlotItemHandler(blockEntity.outputInventory(), i, 62 + col * 18, outputStartY + row * 18) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });
        }

        // Player inventory + hotbar (matches dispenser layout)
        int playerInvY = 84;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, playerInvY + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        this.syncData =
                new ContainerData() {
                    @Override
                    public int get(int index) {
                        return switch (index) {
                            case 0 -> blockEntity.getBlockState().getValue(BeehiveBlock.HONEY_LEVEL);
                            case 1 -> blockEntity.isEmpty() ? 0 : 1;
                            case 2 -> blockEntity.homedBeeCount();
                            case 3 -> blockEntity.analyzedBeeCount();
                            default -> 0;
                        };
                    }

                    @Override
                    public void set(int index, int value) {
                        // Read-only display mirrors block entity / state
                    }

                    @Override
                    public int getCount() {
                        return 4;
                    }
                };
        addDataSlots(syncData);
    }

    public static GeneticApiaryMenu fromNetwork(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
        BlockEntity be = playerInventory.player.level().getBlockEntity(buf.readBlockPos());
        if (!(be instanceof GeneticApiaryBlockEntity apiary)) {
            throw new IllegalStateException("Expected GeneticApiaryBlockEntity at synced position");
        }
        return new GeneticApiaryMenu(containerId, playerInventory, apiary);
    }

    public GeneticApiaryBlockEntity blockEntity() {
        return blockEntity;
    }

    public int honeyLevel() { return syncData.get(0); }
    public boolean hasOccupants() { return syncData.get(1) != 0; }
    public int homedBeeCount() { return syncData.get(2); }
    public int analyzedBeeCount() { return syncData.get(3); }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(levelAccess, player, ModBlocks.GENETIC_APIARY.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack slotStackCopy = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stackInSlot = slot.getItem();
        slotStackCopy = stackInSlot.copy();

        if (index < SLOT_COUNT) {
            if (!moveItemStackTo(stackInSlot, SLOT_COUNT, slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stackInSlot, 0, FRAME_SLOTS, false)) {
            return ItemStack.EMPTY;
        }

        if (stackInSlot.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stackInSlot.getCount() == slotStackCopy.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, stackInSlot);
        return slotStackCopy;
    }
}
