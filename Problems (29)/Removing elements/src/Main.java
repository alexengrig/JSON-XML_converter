// Posted from EduTools plugin
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

class SetUtils {

    public static Set<Integer> getSetFromString(String str) {
        final HashSet<Integer> integers = new HashSet<>();
        for (String s : str.split("\\s+")) {
            integers.add(Integer.parseInt(s));
        }
        return integers;
    }

    public static void removeAllNumbersGreaterThan10(Set<Integer> set) {
        set.removeIf(integer -> integer > 10);
    }

}

/* Do not change code below */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String numbers = scanner.nextLine();
        Set<Integer> set = SetUtils.getSetFromString(numbers);
        SetUtils.removeAllNumbersGreaterThan10(set);
        set.forEach(e -> System.out.print(e + " "));
    }
}