package converter.js;

import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class JsObject extends JsValue {
    protected final List<JsEntity> values;

    public JsObject(List<JsEntity> values) {
        this.values = values;
    }

    public JsObject(JsEntity entity) {
        this(Collections.singletonList(entity));
    }

    public JsObject(String name, JsObject value) {
        this(new JsEntity(name, value));
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public String toPretty() {
        final StringJoiner joiner = new StringJoiner(", ");
        for (JsEntity value : values) {
            joiner.add(value.toPretty());
        }
        return String.format("{%s}", joiner);

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
