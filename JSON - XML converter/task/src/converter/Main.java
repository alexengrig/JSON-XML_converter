package converter;

import converter.js.JsConverter;
import converter.js.JsObject;
import converter.js.JsParser;
import converter.x.XElement;
import converter.x.XPrinter;

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
}
