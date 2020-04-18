package converter.xml;

import converter.Subject;

public class XmlElement extends XmlComplexValue implements Subject {
    protected final String name;
    protected final XmlAttributes attributes;
    protected final XmlValue value;

    public XmlElement(String name) {
        this(name, null, null);
    }

    public XmlElement(String name, XmlAttributes attributes) {
        this(name, attributes, null);
    }

    public XmlElement(String name, XmlValue value) {
        this(name, null, value);
    }

    public XmlElement(String name, XmlAttributes attributes, XmlValue value) {
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
