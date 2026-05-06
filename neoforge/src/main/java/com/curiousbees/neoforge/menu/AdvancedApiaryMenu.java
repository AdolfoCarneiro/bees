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

/**
 * Menu for the Advanced Apiary.
 * Extends GeneticApiaryMenu, adding a bee insertion slot in the bee panel.
 * Players drop a loaded BeeJar or BeeTransporter onto the slot to add a bee as an occupant.
 */
public final class AdvancedApiaryMenu extends GeneticApiaryMenu {

    // Slot position within GUI background coordinates (inside bee panel, bottom area).
    public static final int BEE_INSERT_SLOT_X = 17;
    public static final int BEE_INSERT_SLOT_Y = 56;

    private final int beeInsertSlotIndex;

    public AdvancedApiaryMenu(int containerId, Inventory playerInventory, GeneticApiaryBlockEntity blockEntity) {
        super(ModMenuTypes.ADVANCED_APIARY.get(), containerId, playerInventory, blockEntity);

        this.beeInsertSlotIndex = slots.size();
        // Bee insertion slot — click target only, backed by a transient container.
        addSlot(new Slot(new SimpleContainer(1), 0, BEE_INSERT_SLOT_X, BEE_INSERT_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false; // Insertion handled in clicked() override
            }
            @Override
            public int getMaxStackSize() { return 0; }
            @Override
            public int getMaxStackSize(ItemStack stack) { return 0; }
        });
    }

    public static AdvancedApiaryMenu fromNetwork(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
        BlockEntity be = playerInventory.player.level().getBlockEntity(buf.readBlockPos());
        if (!(be instanceof GeneticApiaryBlockEntity apiary)) {
            throw new IllegalStateException("Expected GeneticApiaryBlockEntity at synced position");
        }
        return new AdvancedApiaryMenu(containerId, playerInventory, apiary);
    }

    public int getBeeInsertSlotIndex() {
        return beeInsertSlotIndex;
    }

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
                        // Reusable — remove component, return item to cursor
                        carried.remove(ModDataComponents.CAPTURED_BEE.get());
                        setCarried(carried);
                    }
                }
            }
            return; // Skip vanilla slot logic — slot is click-target only
        }
        super.clicked(slotId, button, clickType, player);
    }
}
