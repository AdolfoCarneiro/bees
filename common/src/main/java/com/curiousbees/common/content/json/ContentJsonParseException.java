package com.curiousbees.common.content.json;

public final class ContentJsonParseException extends RuntimeException {

    public ContentJsonParseException(String message) {
        super(message);
    }

    public ContentJsonParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
