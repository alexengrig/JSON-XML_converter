class Problem {
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            final String argument = args[i];
            if ("test".equals(argument)) {
                System.out.println(i);
                return;
            }
        }
        System.out.println(-1);
    }
}