package com.curiousbees.common.content.validation;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Accumulates validation errors from a single content loading pass.
 * Errors are never silently discarded.
 */
public final class ContentValidationResult {

    private final List<String> errors;

    private ContentValidationResult(List<String> errors) {
        this.errors = List.copyOf(Objects.requireNonNull(errors));
    }

    public static ContentValidationResult ok() {
        return new ContentValidationResult(List.of());
    }

    public static ContentValidationResult of(List<String> errors) {
        return new ContentValidationResult(errors);
    }

    public boolean isValid() { return errors.isEmpty(); }

    public List<String> errors() { return errors; }

    /**
     * Returns a single combined message for logging.
     * Each error is on its own line, prefixed with "  - ".
     */
    public String combinedMessage() {
        if (errors.isEmpty()) return "OK";
        StringBuilder sb = new StringBuilder();
        for (String e : errors) {
            sb.append("  - ").append(e).append('\n');
        }
        return sb.toString().stripTrailing();
    }

    @Override
    public String toString() {
        return isValid() ? "ContentValidationResult{OK}" : "ContentValidationResult{" + errors.size() + " error(s)}";
    }
}
