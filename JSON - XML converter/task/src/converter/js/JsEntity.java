package converter.js;

import converter.Pretty;

public class JsEntity implements Pretty {
    protected final String name;
    protected final JsValue value;

    public JsEntity(String name, JsValue value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toPretty() {
        return String.format("\"%s\": %s", name, value.toPretty());
    }

    @Override
    public String toString() {
        return String.format("\"%s\": %s", name, value);
    }
}
