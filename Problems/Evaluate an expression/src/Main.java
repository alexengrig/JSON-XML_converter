import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        final double a = scanner.nextDouble();
        final double b = scanner.nextDouble();
        final double c = scanner.nextDouble();
        final double d = scanner.nextDouble();
        System.out.println(a * 10.5 + b * 4.4 + (c + d) / 2.2);
    }
}