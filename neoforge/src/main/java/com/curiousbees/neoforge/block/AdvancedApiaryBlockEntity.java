package com.curiousbees.neoforge.block;

import com.curiousbees.neoforge.menu.AdvancedApiaryMenu;
import com.curiousbees.neoforge.menu.ExpandedApiaryMenu;
import com.curiousbees.neoforge.menu.GeneticApiaryMenu;
import com.curiousbees.neoforge.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Block entity for the Advanced Apiary (ADR-0013).
 * All inventory, production, and automation logic is inherited from
 * {@link GeneticApiaryBlockEntity}. This class exists solely to bind the
 * advanced apiary to its own registered BlockEntityType so NBT round-trips correctly,
 * and to open the {@link AdvancedApiaryMenu} which supports capture-item bee insertion.
 */
public final class AdvancedApiaryBlockEntity extends GeneticApiaryBlockEntity {

    public AdvancedApiaryBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.ADVANCED_APIARY.get();
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        if (hasExpansionBox()) {
            return new ExpandedApiaryMenu(containerId, playerInventory, this);
        }
        return new AdvancedApiaryMenu(containerId, playerInventory, this);
    }

    /** Called when the Extension Box is placed or removed while this container may be open. */
    public void onExpansionChanged() {
        if (level == null || level.isClientSide()) return;
        ServerLevel serverLevel = (ServerLevel) level;
        BlockPos pos = getBlockPos();
        for (ServerPlayer player : serverLevel.players()) {
            if (player.containerMenu instanceof GeneticApiaryMenu gam
                    && gam.blockEntity() == this) {
                player.closeContainer();
                player.openMenu(this, buf -> buf.writeBlockPos(pos));
            }
        }
    }
}
