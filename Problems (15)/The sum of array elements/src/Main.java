import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int size = scanner.nextInt();
        int sum = 0;
        for (int i = 0; i < size; i++) {
            sum += scanner.nextInt();
        }
        System.out.println(sum);
    }
}