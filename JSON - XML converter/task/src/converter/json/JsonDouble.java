package converter.json;

public class JsonDouble extends JsonNumber {
    protected final double value;

    public JsonDouble(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
