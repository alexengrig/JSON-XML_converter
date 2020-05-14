package converter.json;

import converter.Pretty;

public class JsonEntity implements Pretty {
    protected final String name;
    protected final JsonValue value;

    public JsonEntity(String name, JsonValue value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toPretty() {
        return String.format("\"%s\": %s", name, value.toPretty());
    }

    @Override
    public String toPretty(int level) {
        return String.format("\"%s\": %s", name, value.toPretty(level));
    }

    @Override
    public String toString() {
        return String.format("\"%s\": %s", name, value);
    }
}
