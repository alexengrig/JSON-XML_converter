package converter.json;

import converter.Subject;

import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class JsonObject extends JsonValue implements Subject {
    protected final List<JsonEntity> values;

    public JsonObject(List<JsonEntity> values) {
        this.values = values;
    }

    public JsonObject(JsonEntity entity) {
        this(Collections.singletonList(entity));
    }

    public JsonObject(String name, JsonValue value) {
        this(new JsonEntity(name, value));
    }

    @Override
    public String toPretty() {
        final StringJoiner joiner = new StringJoiner(", ");
        for (JsonEntity value : values) {
            joiner.add(value.toPretty());
        }
        return String.format("{%s}", joiner);

    }

    @Override
    public String toPretty(int level) {
        final int nextLevel = level + 1;
        final String nextIndent = INDENT.repeat(nextLevel);
        final StringJoiner joiner = new StringJoiner(",\n" + nextIndent);
        for (JsonEntity value : values) {
            joiner.add(value.toPretty(nextLevel));
        }
        final String indent = INDENT.repeat(level);
        return String.format("{\n" + nextIndent + "%s\n" + indent + "}", joiner);
    }

    @Override
    public String toString() {
        final StringJoiner joiner = new StringJoiner(", ");
        for (JsonEntity value : values) {
            joiner.add(value.toString());
        }
        return String.format("{%s}", joiner);
    }
}
