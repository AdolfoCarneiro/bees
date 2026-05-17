package com.curiousbees.neoforge.datagen;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.neoforge.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public final class CuriousBeesRecipeProvider extends RecipeProvider {

    public CuriousBeesRecipeProvider(PackOutput output,
                                      CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GENETIC_APIARY.get())
            .pattern("PPP")
            .pattern("HHH")
            .pattern("PPP")
            .define('P', ItemTags.PLANKS)
            .define('H', Items.HONEYCOMB)
            .unlockedBy("has_honeycomb", has(Items.HONEYCOMB))
            .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADVANCED_APIARY.get())
            .pattern("SBS")
            .pattern("FAF")
            .pattern("SBS")
            .define('S', Items.SHEARS)
            .define('B', Items.GLASS_BOTTLE)
            .define('F', Items.BLAZE_POWDER)
            .define('A', ModItems.GENETIC_APIARY.get())
            .unlockedBy("has_genetic_apiary", has(ModItems.GENETIC_APIARY.get()))
            .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.APIARY_EXTENSION.get())
            .pattern("IHI")
            .pattern("PHP")
            .pattern("IHI")
            .define('H', Items.HONEYCOMB)
            .define('I', Items.IRON_INGOT)
            .define('P', ItemTags.PLANKS)
            .unlockedBy("has_honeycomb", has(Items.HONEYCOMB))
            .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BEE_TRANSPORTER.get())
            .pattern(" H ")
            .pattern("IGI")
            .define('H', Items.HONEYCOMB)
            .define('I', Items.IRON_INGOT)
            .define('G', Items.GOLD_INGOT)
            .unlockedBy("has_honeycomb", has(Items.HONEYCOMB))
            .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BEE_JAR.get())
            .requires(Items.GLASS_BOTTLE)
            .requires(Items.HONEYCOMB)
            .unlockedBy("has_honeycomb", has(Items.HONEYCOMB))
            .save(output);

        CentrifugeRecipeBuilder.centrifuge(ModItems.MEADOW_COMB.get())
            .output(Items.HONEYCOMB, 1)
            .output(Items.YELLOW_DYE, 1, 0.5f)
            .save(output, rl("centrifuge/meadow_comb"));

        CentrifugeRecipeBuilder.centrifuge(ModItems.FOREST_COMB.get())
            .output(Items.HONEYCOMB, 1)
            .output(Items.GREEN_DYE, 1, 0.5f)
            .save(output, rl("centrifuge/forest_comb"));

        CentrifugeRecipeBuilder.centrifuge(ModItems.ARID_COMB.get())
            .output(Items.HONEYCOMB, 1)
            .output(Items.ORANGE_DYE, 1, 0.5f)
            .save(output, rl("centrifuge/arid_comb"));

        CentrifugeRecipeBuilder.centrifuge(ModItems.CULTIVATED_COMB.get())
            .output(Items.HONEYCOMB, 1)
            .output(Items.WHITE_DYE, 1, 0.5f)
            .save(output, rl("centrifuge/cultivated_comb"));

        CentrifugeRecipeBuilder.centrifuge(ModItems.HARDY_COMB.get())
            .output(Items.HONEYCOMB, 1)
            .output(Items.GRAY_DYE, 1, 0.5f)
            .save(output, rl("centrifuge/hardy_comb"));
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(CuriousBeesMod.MOD_ID, path);
    }
}
