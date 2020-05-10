import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int count = scanner.nextInt();
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < count; i++) {
            final int number = scanner.nextInt();
            if (min > number) {
                min = number;
            }
        }
        System.out.println(min);
    }
}