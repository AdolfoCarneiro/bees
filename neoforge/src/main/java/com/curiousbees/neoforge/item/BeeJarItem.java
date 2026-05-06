package com.curiousbees.neoforge.item;

import net.minecraft.world.item.ItemStack;

/** Single-use capture item — breaks on bee release. */
public final class BeeJarItem extends CapturedBeeItem {

    public BeeJarItem(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemStack releaseItem(ItemStack stack) {
        stack.shrink(1);
        return stack.isEmpty() ? ItemStack.EMPTY : stack;
    }
}
