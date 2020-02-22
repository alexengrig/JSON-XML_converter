package converter.json;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    private static final String NAME_REGEX = "\"[^\"]+\"";
    private static final String VALUE_REGEX = "null|\\d+|\"[^\"]*\"";
    private static final String COLON_REGEX = "\\s*:\\s*";
    private static final String INPUT_REGEX = "^\\{\\s*" + NAME_REGEX + COLON_REGEX +
            "(" + VALUE_REGEX + "" +
            "|\\{\\s*" + NAME_REGEX + COLON_REGEX + "(" + VALUE_REGEX + ")" +
            "(\\s*,\\s*" + NAME_REGEX + COLON_REGEX + "(" + VALUE_REGEX + "))*\\s*})" +
            "(\\s*,\\s*" + NAME_REGEX + COLON_REGEX +
            "(" + VALUE_REGEX + "" +
            "|\\{\\s*" + NAME_REGEX + COLON_REGEX + "(" + VALUE_REGEX + ")" +
            "(\\s*,\\s*" + NAME_REGEX + COLON_REGEX + "(" + VALUE_REGEX + "))*\\s*}))*" +
            "\\s*}$";

    public JsonElement parse(String input) {
        final boolean isValid = input.matches(INPUT_REGEX);
        if (!isValid) {
            throw new IllegalArgumentException("No valid input: " + input);
        }
        final int colonIndex = input.indexOf(":");
        final String rawName = input.substring(0, colonIndex);
        final String elementName = getElementName(rawName);
        final String rawValue = input.substring(colonIndex + 1).trim();
        if (rawValue.startsWith("null")) {
            return new JsonElement(elementName);
        } else if (rawName.startsWith("\"")) {
            final int valueEndIndex = rawName.indexOf("\"", 1);
            final String elementValue = rawValue.substring(1, valueEndIndex);
            return new JsonElement(elementName, elementValue);
        } else if (rawName.startsWith("{")) {
            final String[] chunks = rawValue.replace("{", "").split("\\s*,\\s*");
            final List<JsonEntity> elementEntities = new ArrayList<>();
            for (String chunk : chunks) {
                final JsonEntity jsonEntity = parseEntity(chunk);
                elementEntities.add(jsonEntity);
            }
            return new JsonElement(elementName, new JsonElement(elementEntities));
        } else {
            throw new IllegalArgumentException("No valid value" + rawValue);
        }
    }

    private JsonEntity parseEntity(String input) {
        final int colonIndex = input.indexOf(":");
        final String rawName = input.substring(0, colonIndex);
        final String elementName = getElementName(rawName);
        final String rawValue = input.substring(colonIndex + 1).trim();
        if (rawValue.startsWith("null")) {
            return new JsonEntity(elementName);
        } else if (rawValue.startsWith("\"")) {
            final int valueEndIndex = rawValue.indexOf("\"", 1);
            final String elementValue = rawValue.substring(1, valueEndIndex);
            return new JsonEntity(elementName, elementValue);
        } else {
            return new JsonEntity(elementName, rawValue);
        }
    }

    private String getElementName(String input) {
        final int nameBeginIndex = input.indexOf("\"");
        final int nameEndIndex = input.indexOf("\"", nameBeginIndex + 1);
        return input.substring(nameBeginIndex + 1, nameEndIndex);
    }
}
