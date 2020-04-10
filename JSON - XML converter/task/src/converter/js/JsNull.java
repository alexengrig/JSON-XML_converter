package converter.js;

public class JsNull extends JsValue {
    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public String toString() {
        return "null";
    }
}
