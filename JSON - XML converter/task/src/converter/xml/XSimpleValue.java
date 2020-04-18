package converter.xml;

public class XSimpleValue extends XValue {
    protected final String value;

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
