class StringProcessor extends Thread {

    final Scanner scanner = new Scanner(System.in); // use it to read string from the standard input

    @Override
    public void run() {
        String line;
        char ch;
        boolean finished;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            finished = true;
            for (int i = 0; i < line.length(); i++) {
                ch = line.charAt(i);
                if (Character.isLowerCase(ch)) {
                    System.out.println(line.toUpperCase());
                    finished = false;
                    break;
                }
            }
            if (finished) {
                System.out.println("FINISHED");
                return;
            }
        }
    }
}