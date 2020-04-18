public static boolean checkTheSameNumberOfTimes(int elem, List<Integer> list1, List<Integer> list2) {
    return Collections.frequency(list1, elem) == Collections.frequency(list2, elem);
}