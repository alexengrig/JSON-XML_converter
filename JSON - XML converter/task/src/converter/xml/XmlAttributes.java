package converter.xml;

import java.util.List;
import java.util.StringJoiner;

public class XmlAttributes {
    protected final List<XmlAttribute> values;

    public XmlAttributes(List<XmlAttribute> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        final StringJoiner joiner = new StringJoiner(" ");
        for (XmlAttribute attribute : values) {
            joiner.add(attribute.toString());
        }
        return joiner.toString();
    }
}
