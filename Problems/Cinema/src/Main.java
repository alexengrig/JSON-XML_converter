import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int rows = scanner.nextInt();
        final int cols = scanner.nextInt();
        final int[][] sits = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sits[i][j] = scanner.nextInt();
            }
        }
        final int k = scanner.nextInt();
        final String target = "0".repeat(k);
        for (int i = 0; i < rows; i++) {
            final StringBuilder builder = new StringBuilder();
            for (int j = 0; j < cols; j++) {
                builder.append(sits[i][j]);
            }
            if (builder.toString().contains(target)) {
                System.out.println(i + 1);
                return;
            }
        }
        System.out.println(0);
    }
}