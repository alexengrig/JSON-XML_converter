package converter;

public interface Parser<T extends Subject> {
    T parse(String input);
}
