package converter.x;

import java.util.List;
import java.util.StringJoiner;

public class XAttributes {
    protected final List<XAttribute> values;

    public XAttributes(List<XAttribute> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        final StringJoiner joiner = new StringJoiner(" ");
        for (XAttribute attribute : values) {
            joiner.add(attribute.toString());
        }
        return joiner.toString();
    }
}
