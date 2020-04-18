package converter.json;

public class JsonString extends JsonValue {
    protected final String value;

    public JsonString(String value) {
        this.value = value;
    }

    @Override
    public String toPretty() {
        return String.format("\"%s\"", value);
    }

    @Override
    public String toString() {
        return value;
    }
}
