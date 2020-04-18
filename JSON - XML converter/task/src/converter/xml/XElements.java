package converter.xml;

import java.util.List;

public class XElements extends XComplexValue {
    protected final List<XElement> values;

    public XElements(List<XElement> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (XElement value : values) {
            builder.append(value);
        }
        return builder.toString();
    }
}
