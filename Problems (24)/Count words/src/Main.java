import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

class Main {
    public static void main(String[] args) throws Exception {
        try (Reader reader = new BufferedReader(new InputStreamReader(System.in))) {
            final StringBuilder builder = new StringBuilder();
            final char[] buffer = new char[1024];
            int count;
            while ((count = reader.read(buffer)) != -1) {
                builder.append(buffer, 0, count);
            }
            final String string = builder.toString().trim();
            System.out.println(string.isEmpty() ? 0 : string.split("\\s+").length);
        }
    }

}