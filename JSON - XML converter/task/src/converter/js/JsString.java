package converter.js;

public class JsString extends JsValue {
    protected final String value;

    public JsString(String value) {
        this.value = value;
    }

    @Override
    public String toPretty() {
        return String.format("\"%s\"", value);
    }

    @Override
    public String toString() {
        return value;
    }
}
