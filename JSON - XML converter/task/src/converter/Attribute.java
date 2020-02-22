package converter;

public abstract class Attribute {
    public final String name;
    public final String value;

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
