package converter.x;

public class XSimpleValue extends XValue {
    private final String value;

    public XSimpleValue() {
        this("");
    }

    public XSimpleValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
