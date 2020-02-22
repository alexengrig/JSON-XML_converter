package converter;

public abstract class Element {
    public final String name;
    public final String value;

    public Element(String name) {
        this(name, null);
    }

    public Element(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
