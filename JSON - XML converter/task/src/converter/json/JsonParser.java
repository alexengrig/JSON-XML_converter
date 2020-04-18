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
    private static final String DOT = ".";
    private static final char OPENING_BRACE = '{';
    private static final char CLOSING_BRACE = '}';
    private static final char QUOTE = '"';
    private static final char OPENING_SQUARE_BRACKET = '[';
    private static final char CLOSING_SQUARE_BRACKET = ']';

    @Override
    public JsonObject parse(String input) {
        final List<EntityRaw> rawList = parseObject(input);
        return convertObject(rawList);
    }

    private List<EntityRaw> parseObject(String input) {
        final List<EntityRaw> target = new ArrayList<>();
        final Matcher matcher = NAME_PATTERN.matcher(input);
        Matcher numberMatcher;
        RawType type;
        String name;
        String value;
        int valueBegin;
        int valueEnd;
        int index = 0;
        char ch;
        while (matcher.find(index)) {
            name = getValueFromQuotes(matcher.group());
            valueBegin = matcher.end();
            ch = input.charAt(valueBegin);
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
                numberMatcher = NUMBER_PATTERN.matcher(input);
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
            value = input.substring(valueBegin, valueEnd);
            target.add(new EntityRaw(type, name, value));
            if (valueEnd == input.length() - 1) {
                break;
            }
            index = valueEnd;
        }
        return target;
    }

    private List<ValueRaw> parseArray(String input) {
        final List<ValueRaw> target = new ArrayList<>();
        final String content = getValueFromBrackets(input);
        Matcher numberMatcher;
        RawType type;
        String value;
        int valueBegin;
        int valueEnd;
        char ch;
        for (int i = 0, l = content.length(); i < l; i++) {
            ch = content.charAt(i);
            if (Character.isWhitespace(ch)) continue;
            valueBegin = i;
            ch = content.charAt(valueBegin);
            if (ch == OPENING_BRACE) {
                type = RawType.OBJECT;
                valueEnd = getNestingEnd(content, valueBegin, OPENING_BRACE, CLOSING_BRACE);
            } else if (ch == QUOTE) {
                type = RawType.STRING;
                valueEnd = content.indexOf(QUOTE, valueBegin + 1) + 1;
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
                numberMatcher = NUMBER_PATTERN.matcher(content);
                if (numberMatcher.find(valueBegin)) {
                    valueEnd = numberMatcher.end();
                } else {
                    throw new IllegalArgumentException("Unknown number starting character: " + ch);
                }
            } else if (ch == OPENING_SQUARE_BRACKET) {
                type = RawType.ARRAY;
                valueEnd = getNestingEnd(content, valueBegin, OPENING_SQUARE_BRACKET, CLOSING_SQUARE_BRACKET);
            } else {
                throw new IllegalArgumentException("Unknown value starting character: " + ch);
            }
            value = content.substring(valueBegin, valueEnd);
            target.add(new ValueRaw(type, value));
            if (valueEnd == content.length() - 1) {
                break;
            }
            i = valueEnd;
        }
        return target;
    }

    private JsonObject convertObject(List<EntityRaw> rawList) {
        final ArrayList<JsonEntity> entities = new ArrayList<>(rawList.size());
        for (EntityRaw raw : rawList) {
            final JsonValue value;
            if (raw.type == RawType.OBJECT) {
                value = convertObject(parseObject(raw.value));
            } else if (raw.type == RawType.STRING) {
                value = new JsonString(getValueFromQuotes(raw.value));
            } else if (raw.type == RawType.NULL) {
                value = new JsonNull();
            } else if (raw.type == RawType.BOOLEAN) {
                value = new JsonBoolean(TRUE.equals(raw.value));
            } else if (raw.type == RawType.NUMBER) {
                value = raw.value.contains(DOT)
                        ? new JsonDouble(Double.parseDouble(raw.value))
                        : new JsonInteger(Integer.parseInt(raw.value));
            } else if (raw.type == RawType.ARRAY) {
                final List<ValueRaw> list = parseArray(raw.value);
                value = convertArray(list);
            } else {
                throw new IllegalArgumentException("Unknown raw type: " + raw.type);
            }
            entities.add(new JsonEntity(raw.name, value));
        }
        return new JsonObject(entities);
    }

    private JsonArray convertArray(List<ValueRaw> rawList) {
        final List<JsonValue> target = new ArrayList<>();
        for (ValueRaw raw : rawList) {
            final JsonValue value;
            if (raw.type == RawType.OBJECT) {
                value = convertObject(parseObject(raw.value));
            } else if (raw.type == RawType.STRING) {
                value = new JsonString(getValueFromQuotes(raw.value));
            } else if (raw.type == RawType.NULL) {
                value = new JsonNull();
            } else if (raw.type == RawType.BOOLEAN) {
                value = new JsonBoolean(TRUE.equals(raw.value));
            } else if (raw.type == RawType.NUMBER) {
                value = raw.value.contains(DOT)
                        ? new JsonDouble(Double.parseDouble(raw.value))
                        : new JsonInteger(Integer.parseInt(raw.value));
            } else if (raw.type == RawType.ARRAY) {
                value = convertArray(parseArray(raw.value));
            } else {
                throw new IllegalArgumentException("Unknown raw type: " + raw.type);
            }
            target.add(value);
        }
        return new JsonArray(target);
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

    private String getValueFromBrackets(String input) {
        final int beginIndex = input.indexOf(OPENING_SQUARE_BRACKET) + 1;
        final int endIndex = input.lastIndexOf(CLOSING_SQUARE_BRACKET);
        return input.substring(beginIndex, endIndex).trim();
    }

    private String getValueFromQuotes(String input) {
        final int beginIndex = input.indexOf(QUOTE) + 1;
        final int endIndex = input.lastIndexOf(QUOTE);
        return input.substring(beginIndex, endIndex);
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

    private static class ValueRaw {
        final RawType type;
        final String value;

        private ValueRaw(RawType type, String value) {
            this.type = type;
            this.value = value;
        }
    }
}
