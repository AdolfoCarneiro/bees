package com.curiousbees.common.content.loading;

import java.util.Objects;

public final class ContentDefinitionSource {

    private final String path;
    private final String json;

    public ContentDefinitionSource(String path, String json) {
        this.path = Objects.requireNonNull(path, "path must not be null");
        this.json = Objects.requireNonNull(json, "json must not be null");
        if (path.isBlank()) {
            throw new IllegalArgumentException("path must not be blank");
        }
    }

    public String path() {
        return path;
    }

    public String json() {
        return json;
    }
}
