import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int count = scanner.nextInt();
        final double[] incomes = new double[count];
        for (int i = 0; i < count; i++) {
            incomes[i] = scanner.nextDouble();
        }
        final double[] percents = new double[count];
        for (int i = 0; i < count; i++) {
            percents[i] = scanner.nextDouble();
        }
        double maxTaxes = 0;
        int index = 0;
        for (int i = 0; i < count; i++) {
            final double taxes = (incomes[i] / 100.0) * percents[i];
            if (taxes > maxTaxes) {
                maxTaxes = taxes;
                index = i;
            }
        }
        System.out.println(index + 1);
    }
}