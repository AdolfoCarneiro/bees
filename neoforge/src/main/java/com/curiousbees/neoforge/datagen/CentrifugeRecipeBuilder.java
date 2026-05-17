package com.curiousbees.neoforge.datagen;

import com.curiousbees.neoforge.recipe.CentrifugeRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

public final class CentrifugeRecipeBuilder {

    private final Ingredient ingredient;
    private final int inputCount;
    private int processingTime = 200;
    private int honeyPortions = 1;
    private final List<CentrifugeRecipe.WeightedOutput> outputs = new ArrayList<>();

    private CentrifugeRecipeBuilder(Ingredient ingredient, int inputCount) {
        this.ingredient = ingredient;
        this.inputCount = inputCount;
    }

    public static CentrifugeRecipeBuilder centrifuge(ItemLike ingredient) {
        return new CentrifugeRecipeBuilder(Ingredient.of(ingredient), 1);
    }

    public CentrifugeRecipeBuilder processingTime(int ticks) {
        this.processingTime = ticks;
        return this;
    }

    public CentrifugeRecipeBuilder honeyPortions(int n) {
        this.honeyPortions = n;
        return this;
    }

    public CentrifugeRecipeBuilder output(ItemLike item, int count) {
        outputs.add(new CentrifugeRecipe.WeightedOutput(new ItemStack(item, count), 1.0f));
        return this;
    }

    public CentrifugeRecipeBuilder output(ItemLike item, int count, float chance) {
        outputs.add(new CentrifugeRecipe.WeightedOutput(new ItemStack(item, count), chance));
        return this;
    }

    public void save(RecipeOutput output, ResourceLocation id) {
        CentrifugeRecipe recipe = new CentrifugeRecipe(
            ingredient, inputCount, List.copyOf(outputs), honeyPortions, processingTime);
        output.accept(id, recipe, null);
    }
}
