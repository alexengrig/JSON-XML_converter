import java.util.ArrayList;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int size = scanner.nextInt();
        final ArrayList<Integer> integers = new ArrayList<Integer>(size);
        for (int i = 0; i < size; i++) {
            integers.add(scanner.nextInt());
        }
        final Integer n = scanner.nextInt();
        final int m = scanner.nextInt();
        boolean result = false;
        if (size >= 2) {
            int indexN = 0;
            while ((indexN = integers.indexOf(n)) != -1) {
                if (indexN == 0) {
                    result = integers.get(1) == m;
                } else if (indexN == size - 1) {
                    result = integers.get(size - 2) == m;
                } else {
                    result = integers.get(indexN + 1) == m || integers.get(indexN - 1) == m;
                }
                if (result) {
                    break;
                } else {
                    integers.remove(n);
                }
            }
        }
        System.out.println(result);
    }
}