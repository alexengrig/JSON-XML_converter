package converter.x;

import java.util.List;

public class XListValue extends XValue {
    protected final List<XValue> values;

    public XListValue(List<XValue> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (XValue value : values) {
            builder.append(value);
        }
        return builder.toString();
    }
}
