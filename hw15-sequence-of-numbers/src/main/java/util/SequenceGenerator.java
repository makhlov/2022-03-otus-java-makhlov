package util;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.iterate;

public class SequenceGenerator {
    public static List<Integer> generate(int begin, int step, int amount) {
        Stream<Integer> infinityStream = iterate(begin, e -> e + step);
        return infinityStream.limit(amount).collect(toList());
    }
}