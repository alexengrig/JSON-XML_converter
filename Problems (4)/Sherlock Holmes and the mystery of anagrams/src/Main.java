import java.util.Scanner;
import java.util.stream.Collectors;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final String first = scanner.nextLine();
        final String second = scanner.nextLine();
        final String sortedFirst = first.chars()
                .mapToObj(i -> (char) i)
                .map(Character::toLowerCase)
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining());
        final String sortedSecond = second.chars()
                .mapToObj(i -> (char) i)
                .map(Character::toLowerCase)
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining());
        System.out.println(sortedFirst.equals(sortedSecond) ? "yes" : "no");
    }
}