// Posted from EduTools plugin
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> letters = Arrays.asList(scanner.nextLine().split("\\s+"));
        final int frequency = Collections.frequency(letters, scanner.next());
        System.out.println(frequency);
    }
}