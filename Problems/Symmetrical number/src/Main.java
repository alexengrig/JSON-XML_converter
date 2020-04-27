import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final String line = scanner.nextLine();
        final String number = String.format("%4s", line).replace(" ", "0");
        if (number.charAt(0) == number.charAt(3) && number.charAt(1) == number.charAt(2)) {
            System.out.println(1);
        } else {
            System.out.println(0);
        }
    }
}