import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final String[] words = scanner.nextLine().split("\\s+");
        final TreeMap<String, Integer> map = new TreeMap<>();
        for (String word : words) {
            String target = word.toLowerCase();
            if (map.containsKey(target)) {
                map.put(target, map.get(target) + 1);
            } else {
                map.put(target, 1);
            }
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}