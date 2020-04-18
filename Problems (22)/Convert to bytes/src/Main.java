// Posted from EduTools plugin
import java.io.InputStream;

class Main {
    public static void main(String[] args) throws Exception {
        try (InputStream inputStream = System.in) {
            int read;
            while ((read = inputStream.read()) != -1) {
                System.out.print(read);
            }
        }
    }
}