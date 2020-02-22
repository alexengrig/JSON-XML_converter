package converter.xml;

import converter.Attribute;

public class XmlAttribute extends Attribute {
    public XmlAttribute(String name) {
        this(name, null);
    }

    public XmlAttribute(String name, String value) {
        super(name, value);
    }

    @Override
    public String toString() {
        if (value == null) {
            return name;
        } else {
            return String.format("%s=\"%s\"", name, value);
        }
    }
}
