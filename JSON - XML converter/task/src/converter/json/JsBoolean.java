package converter.json;

public class JsBoolean extends JsValue {
    protected final boolean value;

    public JsBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
