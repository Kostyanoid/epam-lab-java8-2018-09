package streams.part2.exercise;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SuppressWarnings({"unused", "ConstantConditions"})
class Exercise3 {

    @Test
    void createLimitedStringWithOddNumbersSeparatedBySpaces() {
        int countNumbers = 10;

        String result = Stream.iterate(1, i -> i + 1)
                .filter(isOddNumber())
                .limit(countNumbers)
                .map(String::valueOf)
                .collect(joining(" "));

        assertThat(result, is("1 3 5 7 9 11 13 15 17 19"));
    }

    @Test
    void extractEvenNumberedCharactersToNewString() {
        String source = "abcdefghijklm";

        String result = Stream.iterate(0, i -> i + 1)
                .limit(source.length())
                .filter(isEvenNumber())
                .map(source::charAt)
                .collect(new CharJoining());

        assertThat(result, is("acegikm"));
    }

    private static class CharJoining implements Collector<Character, StringBuffer, String> {

        private HashSet<Characteristics> characteristics;

        public CharJoining() {
            characteristics = new HashSet<>();
            characteristics.add(Characteristics.CONCURRENT);
        }

        @Override
        public Supplier<StringBuffer> supplier() {
            return StringBuffer::new;
        }

        @Override
        public BiConsumer<StringBuffer, Character> accumulator() {
            return StringBuffer::append;
        }

        @Override
        public BinaryOperator<StringBuffer> combiner() {
            return StringBuffer::append;
        }

        @Override
        public Function<StringBuffer, String> finisher() {
            return StringBuffer::toString;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return characteristics;
        }
    }

    private static Predicate<Integer> isOddNumber() {
        return n -> n % 2 == 1;
    }

    private static Predicate<Integer> isEvenNumber() {
        return n -> n % 2 == 0;
    }
}
