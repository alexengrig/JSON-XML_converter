package converter.json;

public class JsonElementValue extends JsonValue {
    public final JsonElement value;

    public JsonElementValue(JsonElement value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
