package com.curiousbees.common.content.loading;

import com.curiousbees.common.content.registry.ContentRegistry;

import java.util.List;
import java.util.Objects;

public final class ContentLoadResult {

    private final ContentRegistry registry;
    private final List<String> errors;

    public ContentLoadResult(ContentRegistry registry, List<String> errors) {
        this.registry = Objects.requireNonNull(registry, "registry must not be null");
        this.errors = List.copyOf(Objects.requireNonNull(errors, "errors must not be null"));
    }

    public ContentRegistry registry() {
        return registry;
    }

    public List<String> errors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public String combinedErrorMessage() {
        if (errors.isEmpty()) {
            return "OK";
        }
        StringBuilder message = new StringBuilder();
        for (String error : errors) {
            message.append("  - ").append(error).append('\n');
        }
        return message.toString().stripTrailing();
    }
}
