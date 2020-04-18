// Posted from EduTools plugin
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int count = scanner.nextInt();
        final int[] arr = new int[count];
        for (int i = 0; i < count; i++) {
            arr[i] = scanner.nextInt();
        }
        final int n = scanner.nextInt();
        int sum = 0;
        for (int i : arr) {
            if (i > n) {
                sum += i;
            }
        }
        System.out.println(sum);
    }
}