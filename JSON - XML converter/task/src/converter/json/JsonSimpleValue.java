package converter.json;

public class JsonSimpleValue extends JsonValue {
    public final String value;

    public JsonSimpleValue() {
        this(null);
    }

    public JsonSimpleValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value != null ? String.format("\"%s\"", value) : "null";
    }
}
