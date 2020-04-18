package converter.xml;

public class XAttribute {
    protected final String name;
    protected final String value;

    public XAttribute(String name) {
        this(name, null);
    }

    public XAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return value == null ? name : String.format("%s=\"%s\"", name, value);
    }
}
