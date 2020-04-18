// Posted from EduTools plugin
/**
 * The function accepts a list of mappers and returns an operator that accepts a list of integers
 * and sequentially applies each mapper to each value (perform a transformation)
 */
public static final Function<List<IntUnaryOperator>, UnaryOperator<List<Integer>>> multifunctionalMapper =
        operators -> integers -> integers.stream()
        .map(operators.stream()
        .reduce(IntUnaryOperator.identity(), IntUnaryOperator::andThen)
        ::applyAsInt)
        .collect(Collectors.toList());

/**
 * EXAMPLE: the operator transforms each number to the same number (perform the identity transformation)
 * <p>
 * It returns a list of the same numbers.
 */
public static final UnaryOperator<List<Integer>> identityTransformation =
        multifunctionalMapper.apply(Arrays.asList(x -> x, x -> x, x -> x));

/**
 * The operator accepts an integer list.
 * It multiplies by two each integer number and then add one to its.
 * <p>
 * The operator returns transformed integer list.
 */
public static final UnaryOperator<List<Integer>> multTwoAndThenAddOneTransformation =
        multifunctionalMapper.apply(Arrays.asList(x -> x * 2, x -> x + 1));
/**
 * The operator accepts an integer list.
 * It squares each integer number and then get the next even number following it.
 * <p>
 * The operator returns transformed integer list.
 */
public static final UnaryOperator<List<Integer>> squareAndThenGetNextEvenNumberTransformation =
        multifunctionalMapper.apply(Arrays.asList(x -> x * x, x -> x % 2 == 0 ? x + 2 : x + 1));
