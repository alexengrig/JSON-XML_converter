package converter.xml;

import java.util.List;

public class XmlElements extends XmlComplexValue {
    protected final List<XmlElement> values;

    public XmlElements(List<XmlElement> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (XmlElement value : values) {
            builder.append(value);
        }
        return builder.toString();
    }
}
