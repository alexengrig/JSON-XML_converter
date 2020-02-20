package converter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final String input = scanner.nextLine();
        try {
            String output;
            if (isXML(input)) {
                output = toJSON(input);
            } else if (isJSON(input)) {
                output = toXML(input);
            } else {
                throw new IllegalArgumentException("Unknown type");
            }
            System.out.println(output);
        } catch (Exception e) {
            System.out.println(input);
        }
    }

    private static boolean isJSON(String input) {
        return input.startsWith("{");
    }

    private static boolean isEmptyJSON(String input) {
        return input.contains("null");
    }

    private static String toJSON(String input) {
        if (isEmptyXML(input)) {
            final int tagEndIndex = input.indexOf("/>");
            final String tag = input.substring(1, tagEndIndex).trim();
            return "{\"" + tag + "\":null}";
        } else {
            final int openTagEndIndex = input.indexOf(">");
            final String tag = input.substring(1, openTagEndIndex);
            final int closeTagBeginIndex = input.indexOf("</");
            final String content = input.substring(openTagEndIndex + 1, closeTagBeginIndex);
            return "{\"" + tag + "\":\"" + content + "\"}";
        }
    }

    private static boolean isXML(String input) {
        return input.startsWith("<");
    }

    private static boolean isEmptyXML(String input) {
        return input.contains("/>");
    }

    private static String toXML(String input) {
        final int keyBeginIndex = input.indexOf("\"");
        final int keyEndIndex = input.indexOf("\"", keyBeginIndex + 1);
        final String key = input.substring(keyBeginIndex + 1, keyEndIndex);
        if (isEmptyJSON(input)) {
            return "<" + key + "/>";
        } else {
            final int valueBeginIndex = input.indexOf("\"", keyEndIndex + 1);
            final int valueEndIndex = input.indexOf("\"", valueBeginIndex + 1);
            final String value = input.substring(valueBeginIndex + 1, valueEndIndex);
            return "<" + key + ">" + value + "</" + key + ">";
        }
    }
}
