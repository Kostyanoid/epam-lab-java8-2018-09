package spliterators.exercise;

import java.util.Arrays;
import java.util.Objects;
import java.util.Spliterators;
import java.util.function.IntConsumer;

/**
 * Сплитератор, оборачивающий прямоугольную матрицу int[][]
 * Обходит элементы слева-направо, сверху-вниз
 * Деление "честное" - по количеству элементов
 */
public class FairRectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private static final int THRESHOLD = 10;

    private final int[][] data;
    private final int colsCount;
    private long endExclusive;
    private long current;

    /**
     * 0  1  2  3  4
     * 5  6  / 7  8  9
     * 10 11 12 13 14
     */

    public static FairRectangleSpliterator of(int[][] data) {
        if (data == null || data.length == 0 || data[0].length == 0) throw new IllegalArgumentException();

        int colsCount = data[0].length;
        if (Arrays.stream(data).map(Objects::requireNonNull).anyMatch(row -> row.length != colsCount)) {
            throw new IllegalArgumentException();
        }

        return new FairRectangleSpliterator(data);
    }

    private FairRectangleSpliterator(int[][] data) {
        this(data, 0, data.length * data[0].length);
    }

    private FairRectangleSpliterator(int[][] data, long startInclusive, long endExclusive) {
        super(data.length * data[0].length - (endExclusive - startInclusive), SIZED | NONNULL | ORDERED | IMMUTABLE | CONCURRENT);
        this.data = data;
        this.endExclusive = endExclusive;
        this.colsCount = data[0].length;
        this.current = startInclusive;
    }


    @Override
    public OfInt trySplit() {
        return super.trySplit();
    }

    @Override
    public long estimateSize() {
        return endExclusive - current;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (current == endExclusive) {
            return false;
        }

        int currentRow = (int) current / colsCount;
        int indexInCurrentRow = (int) current % colsCount;

        action.accept(data[currentRow][indexInCurrentRow]);

        current++;

        return current != endExclusive;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        super.forEachRemaining(action);
    }

}