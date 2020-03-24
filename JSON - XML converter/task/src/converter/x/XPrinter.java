package converter.x;

import java.io.PrintStream;

public class XPrinter {
    private PrintStream printer;

    public XPrinter() {
        printer = System.out;
    }

    public void print(XElement element) {
        final String path = element.name;
        final XValue value = element.value;
        printElement();
        printPath(path);
        printValue(value);
        printAttributes(element.attributes);
        printer.println();
        printNext(path, value);
    }

    private void printNext(String path, XValue value) {
        if (value instanceof XElement) {
            print(path, (XElement) value);
        } else if (value instanceof XElements) {
            for (XComplexValue xElement : ((XElements) value).values) {
                print(path, (XElement) xElement);
            }
        }
    }

    private void print(String parentPath, XElement element) {
        final String path = parentPath + ", " + element.name;
        final XValue value = element.value;
        printElement();
        printPath(path);
        printValue(value);
        printAttributes(element.attributes);
        printer.println();
        printNext(path, value);
    }

    private void printElement() {
        printer.println("Element:");
    }

    private void printPath(String path) {
        printer.println("path = " + path);
    }

    private void printValue(XValue value) {
        if (value == null) {
            printer.println("value = null");
        } else if (value instanceof XSimpleValue) {
            final XSimpleValue simpleValue = (XSimpleValue) value;
            printer.println("value = \"" + simpleValue + "\"");
        }
    }

    private void printAttributes(XAttributes attributes) {
        if (attributes != null) {
            printer.println("attributes:");
            for (XAttribute attribute : attributes.values) {
                printer.println(attribute.name + " = \"" + attribute.value + "\"");
            }
        }
    }
}
