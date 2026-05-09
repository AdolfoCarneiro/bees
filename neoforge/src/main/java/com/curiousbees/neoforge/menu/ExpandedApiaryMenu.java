package com.curiousbees.neoforge.menu;

import com.curiousbees.neoforge.block.GeneticApiaryBlockEntity;
import com.curiousbees.neoforge.data.CapturedBeeData;
import com.curiousbees.neoforge.item.BeeJarItem;
import com.curiousbees.neoforge.registry.ModDataComponents;
import com.curiousbees.neoforge.registry.ModMenuTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

/**
 * Menu for the Advanced Apiary when an ApiaryExtensionBlock is placed below it.
 * Adds 3 upgrade slots and shifts player inventory down to accommodate the taller GUI.
 * Bee insert slot is at (17, 91) — bottom of the expanded bee panel.
 */
public final class ExpandedApiaryMenu extends GeneticApiaryMenu {

    public static final int BEE_INSERT_SLOT_X = 17;
    public static final int BEE_INSERT_SLOT_Y = 91;

    private static final int APIARY_END       = 12; // FRAME_SLOTS(3) + OUTPUT_SLOTS(9)
    private static final int PLAYER_INV_START = 12;
    private static final int PLAYER_INV_END   = 48; // 12 + 27 + 9
    private static final int UPGRADE_START    = 48;
    private static final int UPGRADE_END      = 51;

    private final int beeInsertSlotIndex;

    public ExpandedApiaryMenu(int containerId, Inventory playerInventory,
                               GeneticApiaryBlockEntity blockEntity) {
        // playerInvY=112 → hotbarY=170; imageHeight=190
        super(ModMenuTypes.EXPANDED_APIARY.get(), containerId, playerInventory, blockEntity, 112);

        // Upgrade slots row below output grid
        for (int i = 0; i < 3; i++) {
            addSlot(new SlotItemHandler(blockEntity.upgradeInventory(), i, 62 + i * 18, 73));
        }

        // Bee insertion slot — click target only, backed by transient container
        this.beeInsertSlotIndex = slots.size();
        addSlot(new Slot(new SimpleContainer(1), 0, BEE_INSERT_SLOT_X, BEE_INSERT_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) { return false; }
            @Override
            public int getMaxStackSize() { return 0; }
            @Override
            public int getMaxStackSize(ItemStack stack) { return 0; }
        });
    }

    public static ExpandedApiaryMenu fromNetwork(int containerId, Inventory playerInventory,
                                                  RegistryFriendlyByteBuf buf) {
        BlockEntity be = playerInventory.player.level().getBlockEntity(buf.readBlockPos());
        if (!(be instanceof GeneticApiaryBlockEntity apiary)) {
            throw new IllegalStateException("Expected GeneticApiaryBlockEntity at synced position");
        }
        return new ExpandedApiaryMenu(containerId, playerInventory, apiary);
    }

    public int getBeeInsertSlotIndex() { return beeInsertSlotIndex; }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (slotId == beeInsertSlotIndex) {
            ItemStack carried = getCarried();
            if (!carried.isEmpty()
                    && carried.has(ModDataComponents.CAPTURED_BEE.get())
                    && !player.level().isClientSide()) {
                CapturedBeeData data = carried.get(ModDataComponents.CAPTURED_BEE.get());
                if (blockEntity().addOccupantFromCapture(data, (ServerLevel) player.level())) {
                    if (carried.getItem() instanceof BeeJarItem) {
                        carried.shrink(1);
                        setCarried(carried.isEmpty() ? ItemStack.EMPTY : carried);
                    } else {
                        carried.remove(ModDataComponents.CAPTURED_BEE.get());
                        setCarried(carried);
                    }
                }
            }
            return;
        }
        super.clicked(slotId, button, clickType, player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack copy = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        copy = stack.copy();

        if (index < APIARY_END) {
            // Apiary slot (frame/output) → player inventory
            if (!moveItemStackTo(stack, PLAYER_INV_START, PLAYER_INV_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (index >= UPGRADE_START && index < UPGRADE_END) {
            // Upgrade slot → player inventory
            if (!moveItemStackTo(stack, PLAYER_INV_START, PLAYER_INV_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (index >= PLAYER_INV_START && index < PLAYER_INV_END) {
            // Player inventory → try upgrade slots first, then frame slots
            if (!moveItemStackTo(stack, UPGRADE_START, UPGRADE_END, false)) {
                if (!moveItemStackTo(stack, 0, 3 /* FRAME_SLOTS */, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == copy.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, stack);
        return copy;
    }
}
