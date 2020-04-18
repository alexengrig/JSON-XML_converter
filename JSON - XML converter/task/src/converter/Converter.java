package converter;

public interface Converter<T extends Subject, R extends Subject> {
    R convert(T subject);
}
