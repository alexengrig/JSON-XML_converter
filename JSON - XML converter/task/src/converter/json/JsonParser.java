package converter.json;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonParser {
    protected static final Pattern NAME_PATTERN = Pattern.compile("\\s*\"[^\"]*\"\\s*:\\s*");
    protected static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+(\\.\\d+)?");
    protected static final String NULL = "null";
    protected static final String TRUE = "true";
    protected static final String FALSE = "false";
    protected static final char OPENING_BRACE = '{';
    protected static final char CLOSING_BRACE = '}';
    protected static final char QUOTE = '"';
    protected static final char OPENING_SQUARE_BRACKET = '[';
    protected static final char CLOSING_SQUARE_BRACKET = ']';

    public JsonObject parse(String input) {
        final List<EntityRaw> raw = raw(input);
        return convert(raw);
    }

    protected List<EntityRaw> raw(String input) {
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
                int opened = 0;
                int closed = 0;
                int currentIndex = valueBegin + 1;
                while (true) {
                    final int openedIndex = input.indexOf(OPENING_BRACE, currentIndex);
                    final int closingIndex = input.indexOf(CLOSING_BRACE, currentIndex);
                    if (openedIndex == -1 || openedIndex > closingIndex) {
                        if (opened == closed) {
                            valueEnd = closingIndex + 1;
                            break;
                        }
                        closed++;
                        currentIndex = closingIndex + 1;
                    } else {
                        opened++;
                        currentIndex = openedIndex + 1;
                    }
                }
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
                valueEnd = input.indexOf(CLOSING_SQUARE_BRACKET, valueBegin) + 1;
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

    protected JsonObject convert(List<EntityRaw> rawList) {
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
                throw new UnsupportedOperationException("Array is unsupported");
            } else {
                throw new IllegalArgumentException("Unknown raw type: " + raw.type);
            }
            entities.add(new JsonEntity(raw.name, value));
        }
        return new JsonObject(entities);
    }

    protected String getValueFromBraces(String input) {
        return input.substring(input.indexOf("{") + 1, input.lastIndexOf("}"));
    }

    protected String getValueFromQuotes(String input) {
        final int nameBegin = input.indexOf(QUOTE) + 1;
        final int nameEnd = input.indexOf(QUOTE, nameBegin);
        return input.substring(nameBegin, nameEnd);
    }

    protected enum RawType {
        OBJECT,
        STRING,
        NULL,
        BOOLEAN,
        NUMBER,
        ARRAY
    }

    protected static class EntityRaw {
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
