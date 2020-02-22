package converter.json;

import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class JsonElement {
    public final List<JsonEntity> entities;

    public JsonElement(String name, String value) {
        this(Collections.singletonList(new JsonEntity(name, new JsonSimpleValue(value))));
    }

    public JsonElement(String name, JsonElement value) {
        this(Collections.singletonList(new JsonEntity(name, new JsonElementValue(value))));
    }

    public JsonElement(List<JsonEntity> entities) {
        this.entities = entities;
    }

    @Override
    public String toString() {
        final StringJoiner entityJoiner = new StringJoiner(",");
        for (JsonEntity entity : entities) {
            entityJoiner.add(entity.toString());
        }
        return String.format("{%s}", entityJoiner.toString());
    }
}
