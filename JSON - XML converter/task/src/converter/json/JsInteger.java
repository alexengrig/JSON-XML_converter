package converter.json;

public class JsInteger extends JsNumber {
    protected final int value;

    public JsInteger(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
