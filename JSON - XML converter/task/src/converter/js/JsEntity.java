package converter.js;

public class JsEntity {
    protected final String name;
    protected final JsValue value;

    public JsEntity(String name, JsValue value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("\"%s\": %s", name, value);
    }
}
