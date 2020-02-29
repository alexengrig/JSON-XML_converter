import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final String string = scanner.nextLine();
        final int a = scanner.nextInt();
        final int b = scanner.nextInt();
        System.out.println(string.substring(a, b + 1));
    }
}