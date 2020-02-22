package converter.json;

public class JsonEntity {
    public final String name;
    public final JsonValue value;

    public JsonEntity(String name) {
        this(name, new JsonSimpleValue());
    }

    public JsonEntity(String name, String value) {
        this(name, new JsonSimpleValue(value));
    }

    public JsonEntity(String name, JsonElement value) {
        this(name, new JsonElementValue(value));
    }

    public JsonEntity(String name, JsonValue value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("\"%s\":%s", name, value.toString());
    }
}