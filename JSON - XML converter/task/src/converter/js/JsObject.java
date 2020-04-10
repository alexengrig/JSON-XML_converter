package converter.js;

import java.util.List;
import java.util.StringJoiner;

public class JsObject extends JsValue {
    protected final List<JsEntity> values;

    public JsObject(List<JsEntity> values) {
        this.values = values;
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public String toString() {
        final StringJoiner joiner = new StringJoiner(", ");
        for (JsEntity value : values) {
            joiner.add(value.toString());
        }
        return String.format("{%s}", joiner);
    }
}
