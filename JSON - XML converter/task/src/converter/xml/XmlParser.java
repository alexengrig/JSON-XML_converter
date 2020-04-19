package converter.xml;

import converter.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlParser implements Parser<XmlElement> {
    protected static final String START = "<";
    protected static final String END = ">";
    protected static final String START_HEADER = "<?";
    protected static final String END_HEADER = "?>";
    protected static final String SLASH = "/";
    protected static final String START_SLASH = START + SLASH;
    protected static final String SLASH_END = SLASH + END;

    @Override
    public XmlElement parse(String input) {
        final String content = getContent(input);
        final List<String> parts = split(content);
        final List<Raw> rawList = raw(parts);
        return convert(rawList);
    }

    private String getContent(String input) {
        if (input.contains(START_HEADER)) {
            return input.substring(input.indexOf(END_HEADER) + END_HEADER.length());
        }
        return input;
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

    private XmlElement convert(List<Raw> rawList) {
        final Raw parent = rawList.get(0);
        final List<XmlElement> values = new ArrayList<>();
        XmlSimpleValue simpleValue = null;
        for (int i = 1; i < rawList.size() - 1; i++) {
            final Result result = convert(rawList, i);
            if (result.value instanceof XmlSimpleValue) {
                simpleValue = (XmlSimpleValue) result.value;
                break;
            }
            values.add((XmlElement) result.value);
            i = result.lastIndex;
        }
        final XmlElement element;
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
                return new Result(from, new XmlElement(parent.name, getAttributes(parent)));
            } else {
                return new Result(from, new XmlElement(parent.name));
            }
        } else if (parent.type == RawType.VALUE) {
            return new Result(from + 1, new XmlSimpleValue(parent.value));
        } else if (parent.type == RawType.CLOSING) {
            return new Result(from, new XmlSimpleValue());
        }
        final List<XmlElement> values = new ArrayList<>();
        XmlSimpleValue simpleValue = null;
        boolean isOpen = true;
        int countOpen = 1;
        int countClosed = 0;
        int index;
        for (index = from + 1; isOpen && index < rawList.size(); index++) {
            final Raw raw = rawList.get(index);
            switch (raw.type) {
                case EMPTY: {
                    if (hasAttributes(raw.value)) {
                        values.add(new XmlElement(raw.name, getAttributes(raw)));
                    } else {
                        values.add(new XmlElement(raw.name));
                    }
                    break;
                }
                case OPEN: {
                    final Result result = convert(rawList, index + 1);
                    if (hasAttributes(raw.value)) {
                        values.add(new XmlElement(raw.name, getAttributes(raw), result.value));
                    } else {
                        values.add(new XmlElement(raw.name, result.value));
                    }
                    index = result.lastIndex;
                    break;
                }
                case VALUE: {
                    simpleValue = new XmlSimpleValue(raw.value);
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
        final XmlElement element;
        if (simpleValue != null) {
            element = getElement(parent, simpleValue);
        } else {
            element = getElement(parent, getValue(values));
        }
        return new Result(index - 1, element);
    }

    private XmlElement getElement(Raw raw, XmlValue value) {
        if (hasAttributes(raw.value)) {
            return new XmlElement(raw.name, getAttributes(raw), value);
        } else {
            return new XmlElement(raw.name, value);
        }
    }

    private XmlAttributes getAttributes(Raw raw) {
        final int beginIndex = START.length() + raw.name.length();
        final String rawValue = raw.value;
        final int rawLength = rawValue.length();
        final int endIndex = rawValue.endsWith(SLASH_END)
                ? rawLength - SLASH_END.length()
                : rawLength - END.length();
        final String value = rawValue.substring(beginIndex, endIndex);
        final Matcher matcher = Pattern.compile("\\w+(\\s*=\\s*(\"\\w*\"|'\\w*'))?").matcher(value);
        final ArrayList<XmlAttribute> attributes = new ArrayList<>();
        while (matcher.find()) {
            final String rawAttribute = matcher.group();
            attributes.add(getAttribute(rawAttribute));
        }
        return new XmlAttributes(attributes);
    }

    private XmlAttribute getAttribute(String attribute) {
        if (attribute.matches("\\w+\\s*=.*")) {
            final int index = attribute.indexOf("=");
            final String name = attribute.substring(0, index).trim();
            final String quotedValue = attribute.substring(index + 1).trim();
            final String value = quotedValue.substring(1, quotedValue.length() - 1);
            return new XmlAttribute(name, value);
        } else {
            return new XmlAttribute(attribute.trim());
        }
    }

    private XmlValue getValue(List<XmlElement> values) {
        if (values.isEmpty()) {
            return new XmlSimpleValue();
        } else if (values.size() == 1) {
            return values.get(0);
        } else {
            return new XmlElements(values);
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
        final XmlValue value;

        Result(int lastIndex, XmlValue value) {
            this.lastIndex = lastIndex;
            this.value = value;
        }
    }
}
