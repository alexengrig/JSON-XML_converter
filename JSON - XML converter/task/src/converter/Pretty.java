package converter;

public interface Pretty {
    String INDENT = "  ";

    String toPretty();

    default String toPretty(int level) {
        return toPretty();
    }
}
