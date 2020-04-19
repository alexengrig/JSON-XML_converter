package converter.json;

import java.util.List;
import java.util.StringJoiner;

public class JsonArray extends JsonValue {
    protected final List<JsonValue> values;

    public JsonArray(List<JsonValue> values) {
        this.values = values;
    }

    @Override
    public String toPretty() {
        final StringJoiner joiner = new StringJoiner(", ");
        for (JsonValue value : values) {
            joiner.add(value.toPretty());
        }
        return String.format("[%s]", joiner);
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
