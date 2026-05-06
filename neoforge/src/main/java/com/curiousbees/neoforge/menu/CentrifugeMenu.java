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
 *   <li>0 — comb input (insert-only via GUI)
 *   <li>1 — bottle input (insert-only via GUI)
 *   <li>2-5 — output slots (extract-only)
 *   <li>6-32 — player inventory
 *   <li>33-41 — hotbar
 * </ul>
 * synced data: processingProgress (0), processingTotal (1), honeyCounter (2).
 */
public final class CentrifugeMenu extends AbstractContainerMenu {

    static final int MACHINE_SLOT_COUNT =
            CentrifugeBlockEntity.INPUT_SLOTS
            + CentrifugeBlockEntity.BOTTLE_SLOTS
            + CentrifugeBlockEntity.OUTPUT_SLOTS;

    private final CentrifugeBlockEntity blockEntity;
    private final ContainerLevelAccess levelAccess;
    private final ContainerData syncData;

    public CentrifugeMenu(int containerId, Inventory playerInventory, CentrifugeBlockEntity blockEntity) {
        super(ModMenuTypes.CENTRIFUGE.get(), containerId);
        this.blockEntity = Objects.requireNonNull(blockEntity, "blockEntity");
        this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        // Comb input (left)
        addSlot(new SlotItemHandler(blockEntity.inputInventory(), 0, 56, 35));

        // Bottle input (above outputs)
        addSlot(new SlotItemHandler(blockEntity.bottleInventory(), 0, 107, 17));

        // Output slots — 2×2 grid (right)
        for (int i = 0; i < CentrifugeBlockEntity.OUTPUT_SLOTS; i++) {
            int col = i % 2;
            int row = i / 2;
            addSlot(new SlotItemHandler(blockEntity.outputInventory(), i, 116 + col * 18, 35 + row * 18) {
                @Override
                public boolean mayPlace(ItemStack stack) { return false; }
            });
        }

        // Player inventory
        int playerInvY = 84;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, playerInvY + row * 18));
            }
        }
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
            // From player → try comb input first, then bottle slot
            if (!moveItemStackTo(stackInSlot, 0, 2, false)) {
                return ItemStack.EMPTY;
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
