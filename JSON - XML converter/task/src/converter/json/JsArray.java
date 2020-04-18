package converter.json;

import java.util.List;
import java.util.StringJoiner;

public class JsArray extends JsValue {
    protected final List<JsValue> values;

    public JsArray(List<JsValue> values) {
        this.values = values;
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public String toString() {
        final StringJoiner joiner = new StringJoiner(", ");
        for (JsValue value : values) {
            joiner.add(value.toString());
        }
        return String.format("[%s]", joiner);
    }
}
