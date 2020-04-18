// Posted from EduTools plugin
/**
 * Class to modify
 */
class ListMultiplicator {

    /**
     * Multiplies original list provided numper of times
     *
     * @param list list to multiply
     * @param n    times to multiply, should be zero or greater
     */
    public static void multiply(List<?> list, int n) {
        if (n == 0) {
            list.clear();
        } else {
			anotherMultiply(list, n);
        }
    }

	public static <T> void anotherMultiply(List<T> list, int n) {
		final int size = list.size();
		for (int i = 1; i < n; i++) {
			for (int j = 0; j < size; j++) {
				list.add(list.get(j));
			}
		}
	}

}