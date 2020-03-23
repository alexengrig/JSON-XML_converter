package converter;

import converter.json.JsonConverter;
import converter.json.JsonElement;
import converter.json.JsonParser;
import converter.x.XElement;
import converter.x.XParser;
import converter.xml.XmlElement;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        final XElement xml = new XParser().parse("<transaction>\n" +
                "    <id>6753322</id>\n" +
                "    <number region=\"Russia\">8-900-000-00-00</number>\n" +
                "    <nonattr />\n" +
                "    <nonattr></nonattr>\n" +
                "    <nonattr>text</nonattr>\n" +
                "    <attr id=\"1\" />\n" +
                "    <attr id=\"2\"></attr>\n" +
                "    <attr id=\"3\">text</attr>\n" +
                "    <email>\n" +
                "        <to>to_example@gmail.com</to>\n" +
                "        <from>from_example@gmail.com</from>\n" +
                "        <subject>Project discussion</subject>\n" +
                "        <body font=\"Verdana\">Body message</body>\n" +
                "        <date day=\"12\" month=\"12\" year=\"2018\"/>\n" +
                "    </email>\n" +
                "</transaction>");
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
