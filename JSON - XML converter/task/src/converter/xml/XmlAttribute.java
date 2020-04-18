package converter.xml;

public class XmlAttribute {
    protected final String name;
    protected final String value;

    public XmlAttribute(String name) {
        this(name, null);
    }

    public XmlAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return value == null ? name : String.format("%s=\"%s\"", name, value);
    }
}
