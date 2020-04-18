package converter.json;

import converter.Pretty;

public abstract class JsonValue implements Pretty {
    @Override
    public String toPretty() {
        return toString();
    }

    public boolean isSimple() {
        return true;
    }
}
