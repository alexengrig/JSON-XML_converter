package converter;

public abstract class Element extends Value {
    public final String name;
    public final Value value;

    public Element(String name) {
        this(name, null);
    }

    public Element(String name, Value value) {
        this.name = name;
        this.value = value;
    }
}
