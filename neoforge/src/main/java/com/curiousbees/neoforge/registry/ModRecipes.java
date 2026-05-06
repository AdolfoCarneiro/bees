package com.curiousbees.neoforge.registry;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.neoforge.recipe.CentrifugeRecipe;
import com.curiousbees.neoforge.recipe.CentrifugeRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipes {

    private ModRecipes() {}

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, CuriousBeesMod.MOD_ID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, CuriousBeesMod.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<CentrifugeRecipe>> CENTRIFUGE_TYPE =
            RECIPE_TYPES.register("centrifuge",
                    () -> RecipeType.simple(
                            ResourceLocation.fromNamespaceAndPath(CuriousBeesMod.MOD_ID, "centrifuge")));

    public static final DeferredHolder<RecipeSerializer<?>, CentrifugeRecipeSerializer> CENTRIFUGE_SERIALIZER =
            RECIPE_SERIALIZERS.register("centrifuge", CentrifugeRecipeSerializer::new);

    public static void register(IEventBus modEventBus) {
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
    }
}
