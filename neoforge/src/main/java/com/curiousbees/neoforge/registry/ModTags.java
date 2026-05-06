package com.curiousbees.neoforge.registry;

import com.curiousbees.CuriousBeesMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModTags {

    private ModTags() {}

    public static final class Items {

        private Items() {}

        public static final TagKey<Item> FRAMES = TagKey.create(
                Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(CuriousBeesMod.MOD_ID, "frames"));
    }
}
