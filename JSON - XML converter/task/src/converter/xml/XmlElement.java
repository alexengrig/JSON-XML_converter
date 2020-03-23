package converter.xml;

import converter.Element;

import java.util.List;
import java.util.StringJoiner;

public class XmlElement extends Element {
    public final List<XmlAttribute> attributes;

    public XmlElement(String name) {
        this(name, null, null);
    }

    public XmlElement(String name, XmlValue value) {
        this(name, value, null);
    }

    public XmlElement(String name, List<XmlAttribute> attributes) {
        this(name, null, attributes);
    }

    public XmlElement(String name, XmlValue value, List<XmlAttribute> attributes) {
        super(name, value);
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        if (value == null && attributes == null) {
            return String.format("<%s/>", name);
        } else if (value == null) {
            final StringJoiner attributeJoiner = new StringJoiner(" ");
            for (XmlAttribute attribute : attributes) {
                attributeJoiner.add(attribute.toString());
            }
            return String.format("<%s %s/>", name, attributeJoiner.toString());
        } else if (attributes == null) {
            return String.format("<%s>%s</%s>", name, value, name);
        } else {
            final StringJoiner attributeJoiner = new StringJoiner(" ");
            for (XmlAttribute attribute : attributes) {
                attributeJoiner.add(attribute.toString());
            }
            return String.format("<%s %s>%s</%s>", name, attributeJoiner.toString(), value, name);
        }
    }
}
