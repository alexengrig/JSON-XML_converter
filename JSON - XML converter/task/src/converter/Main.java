package converter;

import converter.js.JsConverter;
import converter.js.JsObject;
import converter.js.JsParser;
import converter.x.XConverter;
import converter.x.XElement;
import converter.x.XParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        final String input = getInputFromFile();
        if (isJson(input)) {
            final JsParser parser = new JsParser();
            final JsObject json = parser.parse(input);
            final JsConverter converter = new JsConverter();
            final XElement xml = converter.convert(json);
            System.out.println(xml);
        } else if (isXml(input)) {
            final XParser parser = new XParser();
            final XElement xml = parser.parse(input);
            final XConverter converter = new XConverter();
            final JsObject json = converter.convert(xml);
            System.out.println(json.toPretty());
        } else {
            throw new IllegalArgumentException("Unknown input type: " + input);
        }
    }

    private static String getInputFromFile() throws IOException {
        final File file = new File("test.txt");
        try (final FileReader reader = new FileReader(file)) {
            final StringBuilder builder = new StringBuilder();
            final char[] buffer = new char[1024];
            int count;
            while ((count = reader.read(buffer)) != -1) {
                builder.append(new String(buffer, 0, count));
            }
            return builder.toString();
        }
    }

    private static boolean isJson(String input) {
        return input.startsWith("{");
    }

    private static boolean isXml(String input) {
        return input.startsWith("<");
    }
}
