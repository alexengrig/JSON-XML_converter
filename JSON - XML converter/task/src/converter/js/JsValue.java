package converter.js;

import converter.Pretty;

public abstract class JsValue implements Pretty {
    @Override
    public String toPretty() {
        return toString();
    }

    public boolean isSimple() {
        return true;
    }
}
