import java.time.LocalDateTime;
import java.time.Month;
import java.util.Scanner;

public class Main {

    public static LocalDateTime merge(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        final int year1 = dateTime1.getYear();
        final int year2 = dateTime2.getYear();
        final Month month1 = dateTime1.getMonth();
        final Month month2 = dateTime2.getMonth();
        final int day1 = dateTime1.getDayOfMonth();
        final int day2 = dateTime2.getDayOfMonth();
        final int hour1 = dateTime1.getHour();
        final int hour2 = dateTime2.getHour();
        final int minute1 = dateTime1.getMinute();
        final int minute2 = dateTime2.getMinute();
        final int second1 = dateTime1.getSecond();
        final int second2 = dateTime2.getSecond();
        return LocalDateTime.of(
                Math.max(year1, year2),
                month1.compareTo(month2) >= 0 ? month1 : month2,
                Math.max(day1, day2),
                Math.max(hour1, hour2),
                Math.max(minute1, minute2),
                Math.max(second1, second2));
    }

    /* Do not change code below */
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final LocalDateTime firstDateTime = LocalDateTime.parse(scanner.nextLine());
        final LocalDateTime secondDateTime = LocalDateTime.parse(scanner.nextLine());
        System.out.println(merge(firstDateTime, secondDateTime));
    }
}