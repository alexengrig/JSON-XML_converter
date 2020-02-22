package converter;

import converter.json.JsonConverter;
import converter.json.JsonElement;
import converter.json.JsonParser;
import converter.xml.XmlConverter;
import converter.xml.XmlElement;
import converter.xml.XmlParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        final String input = getInputFromFile();
        if (isXML(input)) {
            final XmlParser xmlParser = new XmlParser();
            final XmlElement xmlElement = xmlParser.parse(input);
            final XmlConverter xmlConverter = new XmlConverter();
            final JsonElement jsonElement = xmlConverter.convertToJson(xmlElement);
            System.out.println(jsonElement);
        } else if (isJSON(input)) {
            final JsonParser jsonParser = new JsonParser();
            final JsonElement jsonElement = jsonParser.parse(input);
            final JsonConverter jsonConverter = new JsonConverter();
            final XmlElement xmlElement = jsonConverter.convert(jsonElement);
            System.out.println(xmlElement);
        } else {
            throw new IllegalArgumentException("Unknown type");
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

    private static boolean isJSON(String input) {
        return input.startsWith("{");
    }

    private static boolean isXML(String input) {
        return input.startsWith("<");
    }
}
