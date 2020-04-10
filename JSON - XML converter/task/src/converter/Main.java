package converter;

import converter.js.JsConverter;
import converter.js.JsObject;
import converter.js.JsParser;
import converter.js.JsPrinter;
import converter.json.JsonConverter;
import converter.json.JsonElement;
import converter.json.JsonParser;
import converter.x.XElement;
import converter.x.XPrinter;
import converter.xml.XmlElement;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        final JsObject json = new JsParser().parse(getInputFromFile());
        final List<XElement> xmlList = new JsConverter().convert(json);
        final XPrinter printer = new XPrinter();
        for (XElement element : xmlList) {
            printer.print(element);
        }
    }

    @NotNull
    private static String getExample1() {
        return "{\n" +
                "  \"transaction\": {\n" +
                "    \"inner13\": {\n" +
                "      \"@invalid_attr\": {\n" +
                "        \"some_key\": \"some value\"\n" +
                "      },\n" +
                "      \"#inner13\": {\n" +
                "        \"key\": \"value\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"\": {\n" +
                "      \"#\": null,\n" +
                "      \"secret\": \"this won't be converted\"\n" +
                "    }\n" +
                "  }\n" +
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
