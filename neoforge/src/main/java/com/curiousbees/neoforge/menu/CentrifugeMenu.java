package com.curiousbees.neoforge.menu;

import com.curiousbees.neoforge.block.CentrifugeBlockEntity;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.Objects;

/**
 * Menu for the Centrifuge.
 *
 * <p>Slot layout:
 * <ul>
 *   <li>0  — comb input (insert-only via GUI)
 *   <li>1  — bottle input (insert-only via GUI)
 *   <li>2-10 — item output slots (9, extract-only, 3x3 grid)
 *   <li>11 — honey bottle output slot (extract-only)
 *   <li>12-14 — upgrade slots (3)
 *   <li>15-41 — player inventory (27)
 *   <li>42-50 — hotbar (9)
 * </ul>
 * synced data: processingProgress (0), processingTotal (1), honeyCounter (2).
 */
public final class CentrifugeMenu extends AbstractContainerMenu {

    static final int MACHINE_SLOT_COUNT =
            CentrifugeBlockEntity.INPUT_SLOTS
            + CentrifugeBlockEntity.BOTTLE_SLOTS
            + CentrifugeBlockEntity.OUTPUT_SLOTS
            + CentrifugeBlockEntity.HONEY_BOTTLE_OUTPUT_SLOTS
            + CentrifugeBlockEntity.UPGRADE_SLOTS; // 15

    private final CentrifugeBlockEntity blockEntity;
    private final ContainerLevelAccess levelAccess;
    private final ContainerData syncData;

    public CentrifugeMenu(int containerId, Inventory playerInventory, CentrifugeBlockEntity blockEntity) {
        super(ModMenuTypes.CENTRIFUGE.get(), containerId);
        this.blockEntity = Objects.requireNonNull(blockEntity, "blockEntity");
        this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        // Slot 0: Comb input (left side)
        addSlot(new SlotItemHandler(blockEntity.inputInventory(), 0, 30, 35));

        // Slot 1: Bottle input (below comb input)
        addSlot(new SlotItemHandler(blockEntity.bottleInventory(), 0, 30, 60));

        // Slots 2-10: 3x3 item output grid (center-right area)
        for (int i = 0; i < CentrifugeBlockEntity.OUTPUT_SLOTS; i++) {
            int col = i % 3;
            int row = i / 3;
            addSlot(new SlotItemHandler(blockEntity.outputInventory(), i, 80 + col * 18, 17 + row * 18) {
                @Override
                public boolean mayPlace(ItemStack stack) { return false; }
            });
        }

        // Slot 11: Honey bottle output (right side, near bottle input)
        addSlot(new SlotItemHandler(blockEntity.honeyBottleOutputInventory(), 0, 150, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) { return false; }
        });

        // Slots 12-14: Upgrade slots (right column)
        for (int i = 0; i < CentrifugeBlockEntity.UPGRADE_SLOTS; i++) {
            addSlot(new SlotItemHandler(blockEntity.upgradeInventory(), i, 155, 60 + i * 18));
        }

        // Player inventory (slots 15-41)
        int playerInvY = 84;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, playerInvY + row * 18));
            }
        }
        // Hotbar (slots 42-50)
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        this.syncData = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> blockEntity.processingProgress();
                    case 1 -> blockEntity.processingTotal();
                    case 2 -> blockEntity.honeyCounter();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {}

            @Override
            public int getCount() { return 3; }
        };
        addDataSlots(syncData);
    }

    public static CentrifugeMenu fromNetwork(int containerId, Inventory playerInventory,
                                              RegistryFriendlyByteBuf buf) {
        BlockEntity be = playerInventory.player.level().getBlockEntity(buf.readBlockPos());
        if (!(be instanceof CentrifugeBlockEntity centrifuge)) {
            throw new IllegalStateException("Expected CentrifugeBlockEntity at synced position");
        }
        return new CentrifugeMenu(containerId, playerInventory, centrifuge);
    }

    public CentrifugeBlockEntity blockEntity()  { return blockEntity; }
    public int processingProgress()             { return syncData.get(0); }
    public int processingTotal()                { return syncData.get(1); }
    public int honeyCounter()                   { return syncData.get(2); }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(levelAccess, player, ModBlocks.CENTRIFUGE.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stackInSlot  = slot.getItem();
        ItemStack slotStackCopy = stackInSlot.copy();

        if (index < MACHINE_SLOT_COUNT) {
            // From machine → player inventory
            if (!moveItemStackTo(stackInSlot, MACHINE_SLOT_COUNT, slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else {
            // From player → try comb input (0) then bottle slot (1) then upgrade slots (12-14)
            if (!moveItemStackTo(stackInSlot, 0, 2, false)) {
                // Try upgrade slots
                if (!moveItemStackTo(stackInSlot, 12, 15, false)) {
                    return ItemStack.EMPTY;
                }
            }
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
