// Posted from EduTools plugin
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final String input = scanner.nextLine();
        if (input.length() > 1) {
            final char[] charArray = input.toCharArray();
            final StringBuilder builder = new StringBuilder();
            char ch = charArray[0];
            int count = 1;
            for (int i = 1, charArrayLength = charArray.length; i < charArrayLength; i++) {
                char current = charArray[i];
                if (ch == current) {
                    count++;
                } else {
                    builder.append(ch).append(count);
                    ch = current;
                    count = 1;
                }
            }
            builder.append(ch).append(count);
            System.out.println(builder);
        } else {
            System.out.println(input + "1");
        }
    }
}