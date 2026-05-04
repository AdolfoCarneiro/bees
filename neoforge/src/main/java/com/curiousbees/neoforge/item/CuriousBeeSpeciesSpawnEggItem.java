package com.curiousbees.neoforge.item;

import com.curiousbees.common.content.builtin.BuiltinBeeContent;
import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

/**
 * Spawns a vanilla {@link Bee} with a fixed Curious Bees species genome.
 * Does not extend {@link net.minecraft.world.item.SpawnEggItem} to avoid registering multiple
 * eggs for {@link EntityType#BEE} in {@code SpawnEggItem}'s static {@code BY_ID} map.
 */
public final class CuriousBeeSpeciesSpawnEggItem extends Item {

    private final BeeSpeciesDefinition species;
    private final int backgroundColor;
    private final int highlightColor;

    public CuriousBeeSpeciesSpawnEggItem(BeeSpeciesDefinition species, int backgroundColor, int highlightColor,
                                         Properties properties) {
        super(properties);
        this.species = Objects.requireNonNull(species, "species");
        this.backgroundColor = backgroundColor;
        this.highlightColor = highlightColor;
    }

    public BeeSpeciesDefinition species() {
        return species;
    }

    /** Tint layer 0 / 1 for {@code minecraft:item/template_spawn_egg} (same convention as {@code SpawnEggItem}). */
    public int eggTint(int tintIndex) {
        return tintIndex == 0 ? backgroundColor : highlightColor;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }
        ItemStack stack = context.getItemInHand();
        BlockPos clicked = context.getClickedPos();
        Direction face = context.getClickedFace();
        BlockState clickedState = level.getBlockState(clicked);
        BlockPos spawnPos;
        if (clickedState.getCollisionShape(level, clicked).isEmpty()) {
            spawnPos = clicked;
        } else {
            spawnPos = clicked.relative(face);
        }
        Entity entity = spawnBee(serverLevel, stack, context.getPlayer(), spawnPos, face, true);
        if (entity == null) {
            return InteractionResult.FAIL;
        }
        gameEvent(serverLevel, context.getPlayer(), entity);
        Player player = context.getPlayer();
        if (player == null || !player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResultHolder.success(stack);
        }
        BlockHitResult hit = getPlayerPOVHitResult(level, player, net.minecraft.world.level.ClipContext.Fluid.SOURCE_ONLY);
        BlockPos pos;
        Direction face = Direction.UP;
        if (hit.getType() == HitResult.Type.BLOCK) {
            pos = hit.getBlockPos();
            face = hit.getDirection();
            if (!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()) {
                pos = pos.relative(face);
            }
        } else if (hit.getType() == HitResult.Type.MISS) {
            Vec3 target = player.getEyePosition().add(player.getLookAngle().scale(2.0));
            pos = BlockPos.containing(target);
        } else {
            return InteractionResultHolder.pass(stack);
        }
        Entity entity = spawnBee(serverLevel, stack, player, pos, face, false);
        if (entity == null) {
            return InteractionResultHolder.pass(stack);
        }
        gameEvent(serverLevel, player, entity);
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResultHolder.success(stack);
    }

    private static void gameEvent(ServerLevel level, @Nullable Player player, Entity entity) {
        level.gameEvent(player, GameEvent.ENTITY_PLACE, entity.position());
    }

    /**
     * Spawns a bee at the given position. Mirrors vanilla spawn egg placement enough for gameplay;
     * returns the entity if spawned, or null if blocked.
     */
    @Nullable
    public Entity spawnBee(ServerLevel level, ItemStack stack, @Nullable Player player,
                           BlockPos pos, Direction face, boolean fromBlockUse) {
        Bee bee = EntityType.BEE.create(level, null, pos, MobSpawnType.SPAWN_EGG, false, false);
        if (bee == null) {
            return null;
        }
        double x = pos.getX() + 0.5;
        double z = pos.getZ() + 0.5;
        int surfaceY = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());
        double y = fromBlockUse
                ? pos.getY() + 0.25
                : Math.max(surfaceY + 0.25, pos.getY() + 0.25);
        bee.moveTo(x, y, z, level.random.nextFloat() * 360F, 0.0F);
        if (player != null) {
            bee.setYRot(player.getYRot());
        }
        Genome genome = BuiltinBeeContent.createDefaultGenome(species, new JavaGeneticRandom(new Random()));
        BeeGenomeStorage.setGenome(bee, genome);
        if (!level.addFreshEntity(bee)) {
            return null;
        }
        return bee;
    }

}
