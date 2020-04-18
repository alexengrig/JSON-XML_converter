package converter.json;

import java.util.List;
import java.util.StringJoiner;

public class JsonArray extends JsonValue {
    protected final List<JsonValue> values;

    public JsonArray(List<JsonValue> values) {
        this.values = values;
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public String toString() {
        final StringJoiner joiner = new StringJoiner(", ");
        for (JsonValue value : values) {
            joiner.add(value.toString());
        }
        return String.format("[%s]", joiner);
    }
}
