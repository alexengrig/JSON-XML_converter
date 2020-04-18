package converter.json;

import converter.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonParser implements Parser<JsonObject> {
    private static final Pattern NAME_PATTERN = Pattern.compile("\\s*\"[^\"]*\"\\s*:\\s*");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+(\\.\\d+)?");
    private static final String NULL = "null";
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final char OPENING_BRACE = '{';
    private static final char CLOSING_BRACE = '}';
    private static final char QUOTE = '"';
    private static final char OPENING_SQUARE_BRACKET = '[';
    private static final char CLOSING_SQUARE_BRACKET = ']';

    @Override
    public JsonObject parse(String input) {
        final List<EntityRaw> raw = raw(input);
        return convert(raw);
    }

    private List<EntityRaw> raw(String input) {
        final List<EntityRaw> rawList = new ArrayList<>();
        final String target = getValueFromBraces(input);
        final Matcher matcher = NAME_PATTERN.matcher(target);
        int index = 0;
        while (matcher.find(index)) {
            final RawType type;
            final String name = getValueFromQuotes(matcher.group());
            final int valueBegin = matcher.end() + 1;
            final int valueEnd;
            final char ch = input.charAt(valueBegin);
            if (ch == OPENING_BRACE) {
                type = RawType.OBJECT;
                valueEnd = getNestingEnd(input, valueBegin, OPENING_BRACE, CLOSING_BRACE);
            } else if (ch == QUOTE) {
                type = RawType.STRING;
                valueEnd = input.indexOf(QUOTE, valueBegin + 1) + 1;
            } else if (ch == NULL.charAt(0)) {
                type = RawType.NULL;
                valueEnd = valueBegin + NULL.length();
            } else if (ch == TRUE.charAt(0)) {
                type = RawType.BOOLEAN;
                valueEnd = valueBegin + TRUE.length();
            } else if (ch == FALSE.charAt(0)) {
                type = RawType.BOOLEAN;
                valueEnd = valueBegin + FALSE.length();
            } else if (Character.isDigit(ch)) {
                type = RawType.NUMBER;
                final Matcher numberMatcher = NUMBER_PATTERN.matcher(input);
                if (numberMatcher.find(valueBegin)) {
                    valueEnd = numberMatcher.end();
                } else {
                    throw new IllegalArgumentException("Unknown number starting character: " + ch);
                }
            } else if (ch == OPENING_SQUARE_BRACKET) {
                type = RawType.ARRAY;
                valueEnd = getNestingEnd(input, valueBegin, OPENING_SQUARE_BRACKET, CLOSING_SQUARE_BRACKET);
            } else {
                throw new IllegalArgumentException("Unknown value starting character: " + ch);
            }
            final String value = input.substring(valueBegin, valueEnd);
            rawList.add(new EntityRaw(type, name, value));
            if (valueEnd == input.length() - 1) {
                break;
            }
            index = valueEnd;
        }
        return rawList;
    }

    private int getNestingEnd(String input, int begin, char opening, char closing) {
        int openedIndex;
        int closingIndex;
        int opened = 0;
        int closed = 0;
        int currentIndex = begin + 1;
        while (true) {
            openedIndex = input.indexOf(opening, currentIndex);
            closingIndex = input.indexOf(closing, currentIndex);
            if (openedIndex == -1 || openedIndex > closingIndex) {
                if (opened == closed) {
                    return closingIndex + 1;
                }
                closed++;
                currentIndex = closingIndex + 1;
            } else {
                opened++;
                currentIndex = openedIndex + 1;
            }
        }
    }

    private JsonObject convert(List<EntityRaw> rawList) {
        final ArrayList<JsonEntity> entities = new ArrayList<>(rawList.size());
        for (EntityRaw raw : rawList) {
            final JsonValue value;
            if (raw.type == RawType.OBJECT) {
                value = convert(raw(raw.value));
            } else if (raw.type == RawType.STRING) {
                value = new JsonString(getValueFromQuotes(raw.value));
            } else if (raw.type == RawType.NULL) {
                value = new JsonNull();
            } else if (raw.type == RawType.BOOLEAN) {
                value = new JsonBoolean("true".equals(raw.value));
            } else if (raw.type == RawType.NUMBER) {
                value = raw.value.contains(".")
                        ? new JsonDouble(Double.parseDouble(raw.value))
                        : new JsonInteger(Integer.parseInt(raw.value));
            } else if (raw.type == RawType.ARRAY) {
                final List<JsonValue> values = new ArrayList<>();
                value = new JsonArray(values);
            } else {
                throw new IllegalArgumentException("Unknown raw type: " + raw.type);
            }
            entities.add(new JsonEntity(raw.name, value));
        }
        return new JsonObject(entities);
    }

    private String getValueFromBraces(String input) {
        return input.substring(input.indexOf("{") + 1, input.lastIndexOf("}"));
    }

    private String getValueFromQuotes(String input) {
        final int nameBegin = input.indexOf(QUOTE) + 1;
        final int nameEnd = input.indexOf(QUOTE, nameBegin);
        return input.substring(nameBegin, nameEnd);
    }

    private enum RawType {
        OBJECT,
        STRING,
        NULL,
        BOOLEAN,
        NUMBER,
        ARRAY
    }

    private static class EntityRaw {
        final RawType type;
        final String name;
        final String value;

        public EntityRaw(RawType type, String name, String value) {
            this.type = type;
            this.name = name;
            this.value = value;
        }

    }
}
