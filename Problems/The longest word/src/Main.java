import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final String line = scanner.nextLine();
        final String[] words = line.split("\\s+");
        Arrays.stream(words).max(Comparator.comparing(String::length)).ifPresent(System.out::println);
    }
}