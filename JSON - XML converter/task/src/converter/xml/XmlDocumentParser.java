package converter.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlDocumentParser {
    public static final String START = "<";
    public static final String END = ">";
    public static final String SLASH = "/";
    public static final String START_SLASH = START + SLASH;
    public static final String SLASH_END = SLASH + END;

    public XmlElement parse(String input) {
        final List<String> parts = split(input);
        final List<Raw> raws = raw(parts);
        final List<?> foo = foo(raws);
        return null;
    }

    private List<String> split(String input) {
        final List<String> substrings = new ArrayList<>();
        String ch = END;
        int begin = 0;
        int end;
        while ((end = input.indexOf(ch, begin)) != -1) {
            final boolean isStart = START.equals(ch);
            ch = isStart ? END : START;
            final String substring = input.substring(begin, (begin = isStart ? end : end + 1));
            if (!substring.isBlank()) {
                substrings.add(substring.trim());
            }
        }
        return substrings;
    }

    private List<Raw> raw(List<String> parts) {
        final List<Raw> raws = new ArrayList<>(parts.size());
        for (String part : parts) {
            if (isTag(part)) {
                final String name = getTagName(part);
                if (isClosingTag(part)) {
                    raws.add(Raw.closing(name, part));
                } else if (isEmptyTag(part)) {
                    raws.add(Raw.empty(name, part));
                } else {
                    raws.add(Raw.open(name, part));
                }
            } else {
                raws.add(Raw.value(part));
            }
        }
        return raws;
    }

    private List<?> foo(List<Raw> raws) {
        for (int i = 0; i < raws.size(); i++) {
            final Raw raw = raws.get(i);
            if (raw.type == RawType.OPEN) {
                final Result go = foo(raws, i + 1);
            }
        }
        return null;
    }

    private Result foo(List<Raw> raws, int from) {
        for (int i = from; i < raws.size(); i++) {
            final Raw raw = raws.get(i);
            if (raw.type == RawType.OPEN) {
                final Result result = foo(raws, i + 1);
                new XmlElement(raw.name);
                i = result.to;
            }
        }
        return null;
    }

    private static class Result {
        final int from;
        final int to;
        final Object value;

        Result(int from, int to, Object value) {
            this.from = from;
            this.to = to;
            this.value = value;
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
}
