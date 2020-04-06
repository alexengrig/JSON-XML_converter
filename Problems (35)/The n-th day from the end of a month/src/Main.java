import java.time.LocalDate;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int year = scanner.nextInt();
        final int month = scanner.nextInt();
        final int offset = scanner.nextInt();
        final LocalDate date = LocalDate.of(year, month, 1);
        System.out.println(date.withDayOfMonth(date.lengthOfMonth()).minusDays(offset - 1));
    }
}