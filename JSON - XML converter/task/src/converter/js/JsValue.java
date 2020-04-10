package converter.js;

public abstract class JsValue {
    public String toPretty() {
        return toString();
    }

    public boolean isSimple() {
        return true;
    }
}
