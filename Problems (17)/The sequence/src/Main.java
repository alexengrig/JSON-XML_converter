import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int size = scanner.nextInt();
        for (int i = 0, number = 1; i < size && number <= size; number++) {
            for (int j = 0; i < size && j < number; i++, j++) {
                System.out.print(number + " ");
            }
        }
    }
}