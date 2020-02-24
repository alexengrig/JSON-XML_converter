import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int count = scanner.nextInt();
        decomposition(count);
    }

    public static void decomposition(int n) {
        decomposition(n, 0, n - 1, "");
        System.out.println(n);
    }

    public static void decomposition(int n, int acc, int max, String prefix) {
        if (max > 1) {
            decomposition(n, acc, max - 1, prefix);
        }
        if (acc + max < n) {
            decomposition(n, acc + max, max, prefix + max + " ");
        } else if (acc + max == n) {
            System.out.println(prefix + max + " ");
        }
    }
}