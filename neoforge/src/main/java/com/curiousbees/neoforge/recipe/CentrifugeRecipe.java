package com.curiousbees.neoforge.recipe;

import com.curiousbees.neoforge.registry.ModRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Recipe for the Centrifuge (E4-T04).
 *
 * <p>JSON layout:
 * <pre>
 * {
 *   "type": "curiousbees:centrifuge",
 *   "ingredient": { "item": "curiousbees:meadow_comb" },
 *   "input_count": 1,
 *   "processing_time": 200,
 *   "honey_portions": 1,
 *   "outputs": [
 *     { "result": { "id": "minecraft:honeycomb", "count": 1 }, "chance": 1.0 }
 *   ]
 * }
 * </pre>
 */
public final class CentrifugeRecipe implements Recipe<SingleRecipeInput> {

    /**
     * One item output with a roll chance.
     * chance=1.0 means always produced; chance=0.5 means 50% per cycle.
     */
    public record WeightedOutput(ItemStack stack, float chance) {
        public static final Codec<WeightedOutput> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                ItemStack.CODEC.fieldOf("result").forGetter(WeightedOutput::stack),
                Codec.FLOAT.optionalFieldOf("chance", 1.0f).forGetter(WeightedOutput::chance)
        ).apply(inst, WeightedOutput::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, WeightedOutput> STREAM_CODEC =
                StreamCodec.composite(
                        ItemStack.STREAM_CODEC, WeightedOutput::stack,
                        ByteBufCodecs.FLOAT,    WeightedOutput::chance,
                        WeightedOutput::new);
    }

    // --- Fields ---

    private final Ingredient ingredient;
    private final int inputCount;
    private final List<WeightedOutput> outputs;
    /** Honey counter portions added per completed cycle (ADR-0015). */
    private final int honeyPortions;
    private final int processingTime;

    public CentrifugeRecipe(Ingredient ingredient, int inputCount,
                             List<WeightedOutput> outputs, int honeyPortions, int processingTime) {
        this.ingredient    = ingredient;
        this.inputCount    = inputCount;
        this.outputs       = List.copyOf(outputs);
        this.honeyPortions = honeyPortions;
        this.processingTime = processingTime;
    }

    // --- Codecs ---

    public static final MapCodec<CentrifugeRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(r -> r.ingredient),
            Codec.INT.optionalFieldOf("input_count", 1).forGetter(r -> r.inputCount),
            WeightedOutput.CODEC.listOf().fieldOf("outputs").forGetter(r -> r.outputs),
            Codec.INT.optionalFieldOf("honey_portions", 0).forGetter(r -> r.honeyPortions),
            Codec.INT.optionalFieldOf("processing_time", 200).forGetter(r -> r.processingTime)
    ).apply(inst, CentrifugeRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CentrifugeRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC,                         r -> r.ingredient,
                    ByteBufCodecs.VAR_INT,                                    r -> r.inputCount,
                    WeightedOutput.STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.outputs,
                    ByteBufCodecs.VAR_INT,                                    r -> r.honeyPortions,
                    ByteBufCodecs.VAR_INT,                                    r -> r.processingTime,
                    CentrifugeRecipe::new);

    // --- Accessors ---

    public Ingredient ingredient()     { return ingredient; }
    public int inputCount()            { return inputCount; }
    public List<WeightedOutput> outputs() { return outputs; }
    public int honeyPortions()         { return honeyPortions; }
    public int processingTime()        { return processingTime; }

    // --- Recipe<SingleRecipeInput> ---

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return ingredient.test(input.item()) && input.item().getCount() >= inputCount;
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input, net.minecraft.core.HolderLookup.Provider registries) {
        return outputs.isEmpty() ? ItemStack.EMPTY : outputs.get(0).stack().copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider registries) {
        return outputs.isEmpty() ? ItemStack.EMPTY : outputs.get(0).stack().copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CENTRIFUGE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CENTRIFUGE_TYPE.get();
    }
}
