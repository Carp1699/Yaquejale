class Predicate {
    public static final TernaryIntPredicate ALL_DIFFERENT = (a, b, c) -> a != b && a != c && b != c;
    @FunctionalInterface
    public interface TernaryIntPredicate {
        boolean test(int a, int b, int c);
    }
}
