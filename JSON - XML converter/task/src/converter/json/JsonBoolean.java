package converter.json;

public class JsonBoolean extends JsonValue {
    protected final boolean value;

    public JsonBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
