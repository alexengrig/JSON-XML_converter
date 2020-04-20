import java.time.LocalDate;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final LocalDate startDate = LocalDate.parse(scanner.nextLine());
        final int days = scanner.nextInt();
        LocalDate date = startDate;
        do {
            System.out.println(date);
            date = date.plusDays(days);
        } while (startDate.getYear() == date.getYear());
    }
}