package converter.xml;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlParser {
    private static final String INPUT_REGEX = "^<[a-zA-Z][\\w_-]*(\\s+[a-zA-Z][\\w_-]*(\\s*=\\s*\"\\s*[\\w'-_]*\\s*\")?)*(>[\\w'-_ ]*</[a-zA-Z][\\w_-]*|\\s*/)>$";
    private static final Pattern ELEMENT_PATTERN = Pattern.compile("[a-zA-Z][\\w_-]*");
    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("\\s+[a-zA-Z][\\w_-]*(\\s*=\\s*\"\\s*[\\w'-_]*\\s*\")?");
    private static final Pattern VALUE_PATTERN = Pattern.compile(">[\\w'-_][\\w'-_ ]*</");

    public XmlElement parse(String input) {
        final boolean isValid = input.matches(INPUT_REGEX);
        if (!isValid) {
            throw new IllegalArgumentException("No valid input: " + input);
        }
        Matcher matcher = ELEMENT_PATTERN.matcher(input);
        if (!matcher.find()) {
            throw new IllegalArgumentException("No valid element: " + input);
        }
        final String elementName = matcher.group();
        final ArrayList<XmlAttribute> elementAttributes = new ArrayList<>();
        matcher = ATTRIBUTE_PATTERN.matcher(input.substring(0, input.indexOf(">")));
        while (matcher.find()) {
            final String attribute = matcher.group();
            if (!attribute.contains("=")) {
                elementAttributes.add(new XmlAttribute(attribute.trim()));
            } else {
                final String[] chunks = attribute.split("\\s*=\\s*");
                final String attributeName = chunks[0].trim();
                final String valueChunk = chunks[1];
                final int beginIndex = valueChunk.indexOf("\"");
                final int endIndex = valueChunk.indexOf("\"", beginIndex + 1);
                final String attributeValue = valueChunk.substring(beginIndex + 1, endIndex);
                if (attributeValue.isBlank()) {
                    elementAttributes.add(new XmlAttribute(attributeName));
                } else {
                    elementAttributes.add(new XmlAttribute(attributeName, attributeValue));
                }
            }
        }
        matcher = VALUE_PATTERN.matcher(input);
        if (!matcher.find()) {
            if (elementAttributes.isEmpty()) {
                return new XmlElement(elementName);
            } else {
                return new XmlElement(elementName, elementAttributes);
            }
        }
        final String value = matcher.group();
        final String elementValue = value.substring(1, value.length() - 2);
        return new XmlElement(elementName, elementValue, elementAttributes);
    }
}
