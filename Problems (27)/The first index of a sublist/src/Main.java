import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final List<String> first = Arrays.asList(scanner.nextLine().split("\\s+"));
        final List<String> second = Arrays.asList(scanner.nextLine().split("\\s+"));
        System.out.print(Collections.indexOfSubList(first, second));
        System.out.print(" ");
        System.out.print(Collections.lastIndexOfSubList(first, second));
    }
}