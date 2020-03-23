package converter.x;

import java.util.List;
import java.util.StringJoiner;

public class XAttributes {
    protected final List<XAttribute> attributes;

    public XAttributes(List<XAttribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        final StringJoiner joiner = new StringJoiner(" ");
        for (XAttribute attribute : attributes) {
            joiner.add(attribute.toString());
        }
        return joiner.toString();
    }
}
