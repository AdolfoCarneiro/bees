package com.curiousbees.neoforge.item;

import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Dispenser behavior for {@link CuriousBeeSpeciesSpawnEggItem}; mirrors vanilla spawn egg dispensing.
 */
public final class CuriousBeeSpawnEggDispenseBehavior extends DefaultDispenseItemBehavior {

    public static final CuriousBeeSpawnEggDispenseBehavior INSTANCE = new CuriousBeeSpawnEggDispenseBehavior();

    private CuriousBeeSpawnEggDispenseBehavior() {}

    @Override
    protected ItemStack execute(BlockSource source, ItemStack stack) {
        if (!(stack.getItem() instanceof CuriousBeeSpeciesSpawnEggItem egg)) {
            return stack;
        }
        Direction facing = source.state().getValue(DispenserBlock.FACING);
        ServerLevel level = source.level();
        var spawnPos = source.pos().relative(facing);
        Entity entity = egg.spawnBee(level, stack, null, spawnPos, facing, true);
        if (entity != null) {
            level.gameEvent(null, GameEvent.ENTITY_PLACE, entity.position());
            stack.shrink(1);
        }
        return stack;
    }
}
