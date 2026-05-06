package com.curiousbees.neoforge.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class CentrifugeRecipeSerializer implements RecipeSerializer<CentrifugeRecipe> {

    @Override
    public MapCodec<CentrifugeRecipe> codec() {
        return CentrifugeRecipe.CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, CentrifugeRecipe> streamCodec() {
        return CentrifugeRecipe.STREAM_CODEC;
    }
}
