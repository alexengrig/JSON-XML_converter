import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int count = scanner.nextInt();
        decomposition(count);
    }

    public static void decomposition(int n) {
        if (n > 1) {
            decomposition(n, n - 1, "");
        }
        System.out.println(n);
    }

    public static void decomposition(int n, int max, String prefix) {
        if (max > 1) {
            decomposition(n, max - 1, prefix);
        }
        if (n - max > 0) {
            decomposition(n - max, max, prefix + max + " ");
        } else if (n - max == 0) {
            System.out.println(prefix + max);
        }
    }
}