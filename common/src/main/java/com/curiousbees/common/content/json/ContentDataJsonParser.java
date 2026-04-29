package com.curiousbees.common.content.json;

import com.curiousbees.common.content.data.MutationDefinitionData;
import com.curiousbees.common.content.data.MutationResultModesData;
import com.curiousbees.common.content.data.ProductionDefinitionData;
import com.curiousbees.common.content.data.ProductionOutputData;
import com.curiousbees.common.content.data.SpeciesDefinitionData;
import com.curiousbees.common.content.data.TraitAlleleDefinitionData;
import com.curiousbees.common.content.data.TraitAllelePairData;
import com.curiousbees.common.content.validation.ContentValidationResult;
import com.curiousbees.common.content.validation.ContentValidator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Parses MVP content JSON into content DTOs.
 * Pure Java by design; platform modules are responsible for discovering files.
 */
public final class ContentDataJsonParser {

    private static final Logger LOGGER = Logger.getLogger(ContentDataJsonParser.class.getName());

    private ContentDataJsonParser() {}

    public static TraitAlleleDefinitionData parseTraitAllele(String json) {
        Map<String, Object> root = parseObjectRoot(json, "trait allele");
        return new TraitAlleleDefinitionData(
                requireString(root, "id", "trait allele"),
                requireString(root, "chromosomeType", "trait allele"),
                requireString(root, "displayName", "trait allele"),
                requireString(root, "dominance", "trait allele"),
                optionalNumberMap(root, "values", "trait allele"));
    }

    public static TraitAlleleDefinitionData parseValidatedTraitAllele(String json) {
        TraitAlleleDefinitionData data = parseTraitAllele(json);
        requireValid(ContentValidator.validateTraitAllele(data), data.id());
        return data;
    }

    public static SpeciesDefinitionData parseSpecies(String json) {
        Map<String, Object> root = parseObjectRoot(json, "species");
        return new SpeciesDefinitionData(
                requireString(root, "id", "species"),
                requireString(root, "displayName", "species"),
                requireString(root, "dominance", "species"),
                requireDefaultTraits(root, "species"),
                optionalStringList(root, "spawnContextNotes", "species"));
    }

    public static SpeciesDefinitionData parseValidatedSpecies(String json, Set<String> knownTraitAlleleIds) {
        Objects.requireNonNull(knownTraitAlleleIds, "knownTraitAlleleIds must not be null");
        SpeciesDefinitionData data = parseSpecies(json);
        requireValid(ContentValidator.validateSpeciesDefinition(data, knownTraitAlleleIds), data.id());
        return data;
    }

    public static MutationDefinitionData parseMutation(String json) {
        Map<String, Object> root = parseObjectRoot(json, "mutation");
        List<String> parents = requireStringList(root, "parents", "mutation");
        if (parents.size() != 2) {
            throw parseError("mutation.parents must contain exactly 2 species ids");
        }

        return new MutationDefinitionData(
                requireString(root, "id", "mutation"),
                parents.get(0),
                parents.get(1),
                requireString(root, "result", "mutation"),
                requireDouble(root, "baseChance", "mutation"),
                requireResultModes(root));
    }

    public static MutationDefinitionData parseValidatedMutation(String json, Set<String> knownSpeciesIds) {
        Objects.requireNonNull(knownSpeciesIds, "knownSpeciesIds must not be null");
        MutationDefinitionData data = parseMutation(json);
        requireValid(ContentValidator.validateMutationDefinition(data, knownSpeciesIds), data.id());
        return data;
    }

    public static ProductionDefinitionData parseProduction(String json) {
        Map<String, Object> root = parseObjectRoot(json, "production");
        return new ProductionDefinitionData(
                requireEitherString(root, "species", "speciesId", "production"),
                requireOutputs(root, "primaryOutputs", "production"),
                optionalOutputs(root, "secondaryOutputs", "production"));
    }

    public static ProductionDefinitionData parseValidatedProduction(String json, Set<String> knownSpeciesIds) {
        Objects.requireNonNull(knownSpeciesIds, "knownSpeciesIds must not be null");
        ProductionDefinitionData data = parseProduction(json);
        requireValid(ContentValidator.validateProductionDefinition(data, knownSpeciesIds), data.speciesId());
        return data;
    }

    private static Map<String, Object> parseObjectRoot(String json, String context) {
        Objects.requireNonNull(json, "json must not be null");
        try {
            Object value = new JsonReader(json).parse();
            if (value instanceof Map<?, ?> map) {
                return castObject(map, context);
            }
            throw parseError(context + " JSON root must be an object");
        } catch (ContentJsonParseException e) {
            LOGGER.warning("Failed to parse " + context + " content JSON: " + e.getMessage());
            throw e;
        }
    }

    private static String requireString(Map<String, Object> root, String field, String context) {
        Object value = root.get(field);
        if (value instanceof String string) {
            return string;
        }
        throw parseError(context + "." + field + " must be a string");
    }

    private static String requireEitherString(Map<String, Object> root, String first, String second, String context) {
        if (root.containsKey(first)) {
            return requireString(root, first, context);
        }
        return requireString(root, second, context);
    }

    private static double requireDouble(Map<String, Object> root, String field, String context) {
        Object value = root.get(field);
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        throw parseError(context + "." + field + " must be a number");
    }

    private static int optionalInt(Map<String, Object> root, String field, String context, int fallback) {
        Object value = root.get(field);
        if (value == null) {
            return fallback;
        }
        if (value instanceof Number number) {
            double doubleValue = number.doubleValue();
            int intValue = number.intValue();
            if (doubleValue == intValue) {
                return intValue;
            }
        }
        throw parseError(context + "." + field + " must be an integer");
    }

    private static List<String> requireStringList(Map<String, Object> root, String field, String context) {
        Object value = root.get(field);
        if (value instanceof List<?> list) {
            return castStringList(list, context + "." + field);
        }
        throw parseError(context + "." + field + " must be an array of strings");
    }

    private static List<String> optionalStringList(Map<String, Object> root, String field, String context) {
        Object value = root.get(field);
        if (value == null) {
            return List.of();
        }
        if (value instanceof List<?> list) {
            return castStringList(list, context + "." + field);
        }
        throw parseError(context + "." + field + " must be an array of strings");
    }

    private static Map<String, Double> optionalNumberMap(Map<String, Object> root, String field, String context) {
        Object value = root.get(field);
        if (value == null) {
            return Map.of();
        }
        if (!(value instanceof Map<?, ?> rawMap)) {
            throw parseError(context + "." + field + " must be an object of numeric values");
        }
        Map<String, Double> values = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : castObject(rawMap, context + "." + field).entrySet()) {
            if (!(entry.getValue() instanceof Number number)) {
                throw parseError(context + "." + field + "." + entry.getKey() + " must be a number");
            }
            values.put(entry.getKey(), number.doubleValue());
        }
        return values;
    }

    private static Map<String, TraitAllelePairData> requireDefaultTraits(Map<String, Object> root, String context) {
        Object value = root.get("defaultTraits");
        if (!(value instanceof Map<?, ?> rawMap)) {
            throw parseError(context + ".defaultTraits must be an object");
        }

        Map<String, TraitAllelePairData> traits = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : castObject(rawMap, context + ".defaultTraits").entrySet()) {
            traits.put(entry.getKey(), parseTraitPair(entry.getValue(),
                    context + ".defaultTraits." + entry.getKey()));
        }
        return traits;
    }

    private static TraitAllelePairData parseTraitPair(Object value, String context) {
        if (value instanceof String alleleId) {
            return new TraitAllelePairData(alleleId, alleleId);
        }
        if (value instanceof List<?> list) {
            List<String> ids = castStringList(list, context);
            if (ids.size() != 2) {
                throw parseError(context + " must contain exactly 2 allele ids");
            }
            return new TraitAllelePairData(ids.get(0), ids.get(1));
        }
        if (value instanceof Map<?, ?> rawMap) {
            Map<String, Object> object = castObject(rawMap, context);
            return new TraitAllelePairData(
                    requireEitherString(object, "first", "alleleA", context),
                    requireEitherString(object, "second", "alleleB", context));
        }
        throw parseError(context + " must be a string, two-element array, or object pair");
    }

    private static MutationResultModesData requireResultModes(Map<String, Object> root) {
        Object value = root.get("resultModes");
        if (!(value instanceof Map<?, ?> rawMap)) {
            throw parseError("mutation.resultModes must be an object");
        }
        Map<String, Object> modes = castObject(rawMap, "mutation.resultModes");
        return new MutationResultModesData(
                requireDouble(modes, "partialChance", "mutation.resultModes"),
                requireDouble(modes, "fullChance", "mutation.resultModes"));
    }

    private static List<ProductionOutputData> requireOutputs(
            Map<String, Object> root, String field, String context) {
        Object value = root.get(field);
        if (value instanceof List<?> list) {
            return parseOutputs(list, context + "." + field);
        }
        throw parseError(context + "." + field + " must be an array of output objects");
    }

    private static List<ProductionOutputData> optionalOutputs(
            Map<String, Object> root, String field, String context) {
        Object value = root.get(field);
        if (value == null) {
            return List.of();
        }
        if (value instanceof List<?> list) {
            return parseOutputs(list, context + "." + field);
        }
        throw parseError(context + "." + field + " must be an array of output objects");
    }

    private static List<ProductionOutputData> parseOutputs(List<?> rawOutputs, String context) {
        List<ProductionOutputData> outputs = new ArrayList<>(rawOutputs.size());
        for (int i = 0; i < rawOutputs.size(); i++) {
            Object rawOutput = rawOutputs.get(i);
            if (!(rawOutput instanceof Map<?, ?> rawMap)) {
                throw parseError(context + "[" + i + "] must be an object");
            }
            Map<String, Object> output = castObject(rawMap, context + "[" + i + "]");
            outputs.add(new ProductionOutputData(
                    requireOutputItem(output, context + "[" + i + "]"),
                    requireDouble(output, "chance", context + "[" + i + "]"),
                    optionalInt(output, "min", context + "[" + i + "]", 1),
                    optionalInt(output, "max", context + "[" + i + "]", 1)));
        }
        return outputs;
    }

    private static String requireOutputItem(Map<String, Object> root, String context) {
        if (root.containsKey("item")) {
            return requireString(root, "item", context);
        }
        if (root.containsKey("outputId")) {
            return requireString(root, "outputId", context);
        }
        throw parseError(context + ".item must be a string");
    }

    private static Map<String, Object> castObject(Map<?, ?> rawMap, String context) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
            if (!(entry.getKey() instanceof String key)) {
                throw parseError(context + " object contains a non-string key");
            }
            result.put(key, entry.getValue());
        }
        return result;
    }

    private static List<String> castStringList(List<?> rawList, String context) {
        List<String> values = new ArrayList<>(rawList.size());
        for (int i = 0; i < rawList.size(); i++) {
            Object item = rawList.get(i);
            if (!(item instanceof String string)) {
                throw parseError(context + "[" + i + "] must be a string");
            }
            values.add(string);
        }
        return values;
    }

    private static void requireValid(ContentValidationResult result, String context) {
        if (result.isValid()) {
            return;
        }
        String message = "Invalid content definition '" + context + "': " + result.combinedMessage();
        LOGGER.warning(message);
        throw new ContentJsonParseException(message);
    }

    private static ContentJsonParseException parseError(String message) {
        return new ContentJsonParseException(message);
    }

    private static final class JsonReader {
        private final String input;
        private int index;

        private JsonReader(String input) {
            this.input = input;
        }

        private Object parse() {
            skipWhitespace();
            Object value = readValue();
            skipWhitespace();
            if (!isAtEnd()) {
                throw error("unexpected trailing content");
            }
            return value;
        }

        private Object readValue() {
            skipWhitespace();
            if (isAtEnd()) {
                throw error("unexpected end of JSON");
            }
            char current = input.charAt(index);
            return switch (current) {
                case '{' -> readObject();
                case '[' -> readArray();
                case '"' -> readString();
                case 't' -> readLiteral("true", Boolean.TRUE);
                case 'f' -> readLiteral("false", Boolean.FALSE);
                case 'n' -> readLiteral("null", null);
                default -> {
                    if (current == '-' || Character.isDigit(current)) {
                        yield readNumber();
                    }
                    throw error("unexpected character '" + current + "'");
                }
            };
        }

        private Map<String, Object> readObject() {
            expect('{');
            Map<String, Object> object = new LinkedHashMap<>();
            skipWhitespace();
            if (consume('}')) {
                return object;
            }
            while (true) {
                skipWhitespace();
                if (peek() != '"') {
                    throw error("object keys must be strings");
                }
                String key = readString();
                skipWhitespace();
                expect(':');
                object.put(key, readValue());
                skipWhitespace();
                if (consume('}')) {
                    return object;
                }
                expect(',');
            }
        }

        private List<Object> readArray() {
            expect('[');
            List<Object> array = new ArrayList<>();
            skipWhitespace();
            if (consume(']')) {
                return array;
            }
            while (true) {
                array.add(readValue());
                skipWhitespace();
                if (consume(']')) {
                    return array;
                }
                expect(',');
            }
        }

        private String readString() {
            expect('"');
            StringBuilder builder = new StringBuilder();
            while (!isAtEnd()) {
                char current = input.charAt(index++);
                if (current == '"') {
                    return builder.toString();
                }
                if (current != '\\') {
                    builder.append(current);
                    continue;
                }
                if (isAtEnd()) {
                    throw error("unterminated escape sequence");
                }
                char escaped = input.charAt(index++);
                switch (escaped) {
                    case '"' -> builder.append('"');
                    case '\\' -> builder.append('\\');
                    case '/' -> builder.append('/');
                    case 'b' -> builder.append('\b');
                    case 'f' -> builder.append('\f');
                    case 'n' -> builder.append('\n');
                    case 'r' -> builder.append('\r');
                    case 't' -> builder.append('\t');
                    case 'u' -> builder.append(readUnicodeEscape());
                    default -> throw error("unsupported escape sequence \\" + escaped);
                }
            }
            throw error("unterminated string");
        }

        private char readUnicodeEscape() {
            if (index + 4 > input.length()) {
                throw error("incomplete unicode escape");
            }
            String hex = input.substring(index, index + 4);
            index += 4;
            try {
                return (char) Integer.parseInt(hex, 16);
            } catch (NumberFormatException e) {
                throw new ContentJsonParseException("invalid unicode escape at position " + (index - 4), e);
            }
        }

        private Object readLiteral(String literal, Object value) {
            if (!input.startsWith(literal, index)) {
                throw error("expected literal " + literal);
            }
            index += literal.length();
            return value;
        }

        private Number readNumber() {
            int start = index;
            consume('-');
            readDigits();
            if (consume('.')) {
                readDigits();
            }
            if (consume('e') || consume('E')) {
                if (peek() == '+' || peek() == '-') {
                    index++;
                }
                readDigits();
            }
            String number = input.substring(start, index);
            try {
                return Double.valueOf(number);
            } catch (NumberFormatException e) {
                throw new ContentJsonParseException("invalid number at position " + start + ": " + number, e);
            }
        }

        private void readDigits() {
            int start = index;
            while (!isAtEnd() && Character.isDigit(input.charAt(index))) {
                index++;
            }
            if (start == index) {
                throw error("expected digit");
            }
        }

        private void skipWhitespace() {
            while (!isAtEnd() && Character.isWhitespace(input.charAt(index))) {
                index++;
            }
        }

        private void expect(char expected) {
            if (!consume(expected)) {
                throw error("expected '" + expected + "'");
            }
        }

        private boolean consume(char expected) {
            if (!isAtEnd() && input.charAt(index) == expected) {
                index++;
                return true;
            }
            return false;
        }

        private char peek() {
            return isAtEnd() ? '\0' : input.charAt(index);
        }

        private boolean isAtEnd() {
            return index >= input.length();
        }

        private ContentJsonParseException error(String message) {
            return new ContentJsonParseException(message + " at position " + index);
        }
    }
}
