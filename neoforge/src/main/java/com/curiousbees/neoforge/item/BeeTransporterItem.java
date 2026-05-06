package com.curiousbees.neoforge.item;

import com.curiousbees.neoforge.registry.ModDataComponents;
import net.minecraft.world.item.ItemStack;

/** Reusable capture item — clears stored bee data on release, item is kept. */
public final class BeeTransporterItem extends CapturedBeeItem {

    public BeeTransporterItem(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemStack releaseItem(ItemStack stack) {
        stack.remove(ModDataComponents.CAPTURED_BEE.get());
        return stack;
    }
}
