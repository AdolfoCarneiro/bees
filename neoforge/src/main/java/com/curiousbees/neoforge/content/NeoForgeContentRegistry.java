package com.curiousbees.neoforge.content;

import com.curiousbees.common.content.loading.ContentLoadResult;
import com.curiousbees.common.content.registry.ContentRegistry;

import java.util.Objects;

public final class NeoForgeContentRegistry {

    private static volatile ContentRegistry current = ContentRegistry.builtIn();

    private NeoForgeContentRegistry() {}

    public static ContentRegistry current() {
        return current;
    }

    public static void apply(ContentLoadResult result) {
        Objects.requireNonNull(result, "result must not be null");
        current = result.registry();
    }
}
