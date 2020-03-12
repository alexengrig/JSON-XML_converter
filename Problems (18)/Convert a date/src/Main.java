import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final LocalDate date = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.println(date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    }
}