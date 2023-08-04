import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

class PrefixSuffixOperator {


    public static void main(String[] args) {
        BiFunction<Integer,Integer,Integer> max = Integer::max;
        System.out.println(max.apply(3,10));
    }
}