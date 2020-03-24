package converter.x;

import java.util.List;

public class XElements extends XComplexValue {
    protected final List<XComplexValue> values;

    public XElements(List<XComplexValue> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (XComplexValue value : values) {
            builder.append(value);
        }
        return builder.toString();
    }
}
