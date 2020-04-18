// Posted from EduTools plugin
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int x1 = scanner.nextInt();
        final int y1 = scanner.nextInt();
        final int z1 = scanner.nextInt();
        final int x2 = scanner.nextInt();
        final int y2 = scanner.nextInt();
        final int z2 = scanner.nextInt();
        final int[] sizes1 = {x1, y1, z1};
        final int[] sizes2 = {x2, y2, z2};
        Arrays.sort(sizes1);
        Arrays.sort(sizes2);
        if (Arrays.equals(sizes1, sizes2)) {
            System.out.println("Box 1 = Box 2");
        } else if (sizes1[2] <= sizes2[2] && sizes1[0] <= sizes2[0]) {
            System.out.println("Box 1 < Box 2");
        } else if (sizes1[2] >= sizes2[2] && sizes1[0] >= sizes2[0]) {
            System.out.println("Box 1 > Box 2");
        } else {
            System.out.println("Incomparable");
        }
    }
}