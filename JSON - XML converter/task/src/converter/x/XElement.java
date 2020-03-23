package converter.x;

public class XElement extends XValue {
    private final String name;
    private final XAttributes attributes;
    private final XValue value;

    public XElement(String name) {
        this(name, null, null);
    }

    public XElement(String name, XAttributes attributes) {
        this(name, attributes, null);
    }

    public XElement(String name, XValue value) {
        this(name, null, value);
    }

    public XElement(String name, XAttributes attributes, XValue value) {
        this.name = name;
        this.attributes = attributes;
        this.value = value;
    }

    @Override
    public String toString() {
        if (attributes == null && value == null) {
            return String.format("<%s/>", name);
        } else if (value == null) {
            return String.format("<%s %s/>", name, attributes);
        } else if (attributes == null) {
            return String.format("<%s>%s</%s>", name, value, name);
        } else {
            return String.format("<%s %s>%s</%s>", name, attributes, value, name);
        }
    }
}
