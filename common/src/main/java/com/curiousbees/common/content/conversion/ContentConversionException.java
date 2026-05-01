package com.curiousbees.common.content.conversion;

/**
 * Thrown when a validated content DTO cannot be converted into a runtime domain definition.
 * This indicates a bug in the validation logic (a DTO passed validation but conversion failed).
 */
public final class ContentConversionException extends RuntimeException {

    public ContentConversionException(String message) {
        super(message);
    }

    public ContentConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
