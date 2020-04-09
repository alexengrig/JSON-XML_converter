package converter;

import converter.js.JsObject;
import converter.js.JsParser;
import converter.js.JsPrinter;
import converter.json.JsonConverter;
import converter.json.JsonElement;
import converter.json.JsonParser;
import converter.xml.XmlElement;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        final JsObject json = new JsParser().parse(getExample1());
        new JsPrinter().print(json);
    }

    @NotNull
    private static String getExample1() {
        return "{\n" +
                "    \"key\": {\n" +
                "        \"child_key1\": {\n" +
                "            \"@attribute1\": \"value1\",\n" +
                "            \"@attribute2\": \"value2\",\n" +
                "            \"#child_key1\": \"value3\"\n" +
                "        },\n" +
                "        \"child_key2\": \"child_key_value\",\n" +
                "        \"child_key3\": {\n" +
                "            \"@attribute1\": \"value4\",\n" +
                "            \"@attribute2\": \"value5\",\n" +
                "            \"#child_key3\": null\n" +
                "        },\n" +
                "        \"child_key4\": {\n" +
                "            \"child_child_key1\": \"value1\",\n" +
                "            \"child_child_key2\": \"value2\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    private static void task() throws IOException {
        final String input = getInputFromFile();
        if (isXML(input)) {

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
