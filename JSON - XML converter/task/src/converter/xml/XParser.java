package converter.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XParser {
    protected static final String START = "<";
    protected static final String END = ">";
    protected static final String SLASH = "/";
    protected static final String START_SLASH = START + SLASH;
    protected static final String SLASH_END = SLASH + END;

    public XElement parse(String input) {
        final List<String> parts = split(input);
        final List<Raw> rawList = raw(parts);
        return convert(rawList);
    }

    private List<String> split(String input) {
        final List<String> substrings = new ArrayList<>();
        String ch = END;
        int begin = 0;
        int end;
        while ((end = input.indexOf(ch, begin)) != -1) {
            final boolean isStart = START.equals(ch);
            ch = isStart ? END : START;
            final String substring = input.substring(begin, (begin = isStart ? end : end + 1)).trim();
            if (!substring.isEmpty()) {
                substrings.add(substring);
            }
        }
        return substrings;
    }

    private List<Raw> raw(List<String> parts) {
        final List<Raw> list = new ArrayList<>(parts.size());
        for (String part : parts) {
            if (isTag(part)) {
                final String name = getTagName(part);
                if (isClosingTag(part)) {
                    list.add(Raw.closing(name, part));
                } else if (isEmptyTag(part)) {
                    list.add(Raw.empty(name, part));
                } else {
                    list.add(Raw.open(name, part));
                }
            } else {
                list.add(Raw.value(part));
            }
        }
        return list;
    }

    private XElement convert(List<Raw> rawList) {
        final Raw parent = rawList.get(0);
        final List<XElement> values = new ArrayList<>();
        XSimpleValue simpleValue = null;
        for (int i = 1; i < rawList.size() - 1; i++) {
            final Result result = convert(rawList, i);
            if (result.value instanceof XSimpleValue) {
                simpleValue = (XSimpleValue) result.value;
                break;
            }
            values.add((XElement) result.value);
            i = result.lastIndex;
        }
        final XElement element;
        if (simpleValue != null) {
            element = getElement(parent, simpleValue);
        } else {
            element = getElement(parent, getValue(values));
        }
        return element;
    }

    private Result convert(List<Raw> rawList, int from) {
        final Raw parent = rawList.get(from);
        if (parent.type == RawType.EMPTY) {
            if (hasAttributes(parent.value)) {
                return new Result(from, new XElement(parent.name, getAttributes(parent)));
            } else {
                return new Result(from, new XElement(parent.name));
            }
        } else if (parent.type == RawType.VALUE) {
            return new Result(from + 1, new XSimpleValue(parent.value));
        } else if (parent.type == RawType.CLOSING) {
            return new Result(from, new XSimpleValue());
        }
        final List<XElement> values = new ArrayList<>();
        XSimpleValue simpleValue = null;
        boolean isOpen = true;
        int countOpen = 1;
        int countClosed = 0;
        int index;
        for (index = from + 1; isOpen && index < rawList.size(); index++) {
            final Raw raw = rawList.get(index);
            switch (raw.type) {
                case EMPTY: {
                    if (hasAttributes(raw.value)) {
                        values.add(new XElement(raw.name, getAttributes(raw)));
                    } else {
                        values.add(new XElement(raw.name));
                    }
                    break;
                }
                case OPEN: {
                    final Result result = convert(rawList, index + 1);
                    if (hasAttributes(raw.value)) {
                        values.add(new XElement(raw.name, getAttributes(raw), result.value));
                    } else {
                        values.add(new XElement(raw.name, result.value));
                    }
                    index = result.lastIndex;
                    break;
                }
                case VALUE: {
                    simpleValue = new XSimpleValue(raw.value);
                    isOpen = false;
                    index++;
                    break;
                }
                case CLOSING: {
                    if (parent.name.equals(raw.name) && countOpen == ++countClosed) {
                        isOpen = false;
                    }
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknown raw type: " + raw.type);
                }
            }
        }
        final XElement element;
        if (simpleValue != null) {
            element = getElement(parent, simpleValue);
        } else {
            element = getElement(parent, getValue(values));
        }
        return new Result(index - 1, element);
    }

    private XElement getElement(Raw raw, XValue value) {
        if (hasAttributes(raw.value)) {
            return new XElement(raw.name, getAttributes(raw), value);
        } else {
            return new XElement(raw.name, value);
        }
    }

    private XAttributes getAttributes(Raw raw) {
        final int beginIndex = START.length() + raw.name.length();
        final String rawValue = raw.value;
        final int rawLength = rawValue.length();
        final int endIndex = rawValue.endsWith(SLASH_END)
                ? rawLength - SLASH_END.length()
                : rawLength - END.length();
        final String value = rawValue.substring(beginIndex, endIndex);
        final Matcher matcher = Pattern.compile("\\w+(\\s*=\\s*\"\\w*\")?").matcher(value);
        final ArrayList<XAttribute> attributes = new ArrayList<>();
        while (matcher.find()) {
            final String rawAttribute = matcher.group();
            attributes.add(getAttribute(rawAttribute));
        }
        return new XAttributes(attributes);
    }

    private XAttribute getAttribute(String attribute) {
        if (attribute.matches("\\w+\\s*=.*")) {
            final int index = attribute.indexOf("=");
            final String name = attribute.substring(0, index).trim();
            final String quotedValue = attribute.substring(index + 1).trim();
            final String value = quotedValue.substring(1, quotedValue.length() - 1);
            return new XAttribute(name, value);
        } else {
            return new XAttribute(attribute.trim());
        }
    }

    private XValue getValue(List<XElement> values) {
        if (values.isEmpty()) {
            return new XSimpleValue();
        } else if (values.size() == 1) {
            return values.get(0);
        } else {
            return new XElements(values);
        }
    }

    private boolean isTag(String tag) {
        return tag.startsWith(START) && tag.endsWith(END);
    }

    private boolean isEmptyTag(String tag) {
        return tag.startsWith(START) && tag.endsWith(SLASH_END);
    }

    private boolean isClosingTag(String tag) {
        return tag.startsWith(START_SLASH) && tag.endsWith(END);
    }

    private boolean hasAttributes(String tag) {
        return tag.matches("<\\w+\\s+\\w.*/?>");
    }

    private String getTagName(String tag) {
        final Matcher matcher = Pattern.compile("(\\s|" + SLASH_END + "|" + END + ")").matcher(tag);
        if (matcher.find()) {
            final int begin = tag.startsWith(START_SLASH) ? START_SLASH.length() : START.length();
            final int end = matcher.start();
            return tag.substring(begin, end);
        }
        throw new IllegalArgumentException("Illegal tag: " + tag);
    }

    private enum RawType {
        OPEN, CLOSING, EMPTY, VALUE
    }

    private static class Raw {
        final String name;
        final String value;
        final RawType type;

        Raw(String name, String value, RawType type) {
            this.name = name;
            this.value = value;
            this.type = type;
        }

        static Raw open(String name, String value) {
            return new Raw(name, value, RawType.OPEN);
        }

        static Raw closing(String name, String value) {
            return new Raw(name, value, RawType.CLOSING);
        }

        static Raw empty(String name, String value) {
            return new Raw(name, value, RawType.EMPTY);
        }

        static Raw value(String value) {
            return new Raw("", value, RawType.VALUE);
        }
    }

    private static class Result {
        final int lastIndex;
        final XValue value;

        Result(int lastIndex, XValue value) {
            this.lastIndex = lastIndex;
            this.value = value;
        }
    }
}
