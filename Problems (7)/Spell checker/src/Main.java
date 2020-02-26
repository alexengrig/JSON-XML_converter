import java.util.*;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int count = Integer.parseInt(scanner.nextLine().trim());
        final List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine().trim().toLowerCase());
        }
        final List<String> words = lines.subList(0, count);
        final List<String> anotherWords = lines.subList(count + 1, lines.size());
        final Set<String> set = new HashSet<>();
        for (String anotherWord : anotherWords) {
            for (String s : anotherWord.split("\\s+")) {
                if (!words.contains(s)) {
                    set.add(s);
                }
            }
        }
        for (String s : set) {
            System.out.println(s);
        }
    }
}