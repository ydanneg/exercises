import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javafx.util.Pair;
import org.junit.Test;

public class MatrixLayerRotation {

    private static final boolean TRACE = Boolean.parseBoolean("true");

    @Test
    public void test1() {
        List<List<Integer>> matrix = new ArrayList<List<Integer>>() {{
            add(Arrays.asList(1, 2, 3, 4));
            add(Arrays.asList(12, 1, 2, 5));
            add(Arrays.asList(11, 4, 3, 6));
            add(Arrays.asList(10, 9, 8, 7));
        }};
        List<List<Integer>> expected = new ArrayList<List<Integer>>() {{
            add(Arrays.asList(2, 3, 4, 5));
            add(Arrays.asList(1, 2, 3, 6));
            add(Arrays.asList(12, 1, 4, 7));
            add(Arrays.asList(11, 10, 9, 8));
        }};

        traceMatrix(matrix, "input:");
        rotate(matrix, 1);
        traceMatrix(matrix, "output:");
        traceMatrix(expected, "expected:");

        assertThat(matrix, is(equalTo(expected)));
    }


    @Test
    public void test2() {
        List<List<Integer>> input = new ArrayList<List<Integer>>() {{
            add(Arrays.asList(1, 2, 3, 4));
            add(Arrays.asList(7, 8, 9, 10));
            add(Arrays.asList(13, 14, 15, 16));
            add(Arrays.asList(19, 20, 21, 22));
            add(Arrays.asList(25, 26, 27, 28));
        }};
        List<List<Integer>> expected = new ArrayList<List<Integer>>() {{
            add(Arrays.asList(28, 27, 26, 25));
            add(Arrays.asList(22, 9, 15, 19));
            add(Arrays.asList(16, 8, 21, 13));
            add(Arrays.asList(10, 14, 20, 7));
            add(Arrays.asList(4, 3, 2, 1));
        }};

        traceMatrix(input, "input:");
        rotate(input, 7);
        traceMatrix(input, "output:");
        traceMatrix(expected, "expected:");

        assertThat(input, is(equalTo(expected)));
    }

    private static void rotate(List<List<Integer>> matrix, int offset) {
        if (offset == 0) {
            return;
        }
        int m = matrix.size();
        int n = matrix.get(0).size();
        for (int i = 0; i < Math.min(m, n) / 2; i++) {
            List<Pair<Integer, Integer>> indexes = borderIndexes(m, n, i);
            List<Integer> values = indexedValues(matrix, indexes);
            Collections.rotate(values, -offset);

            for (int j = 0; j < values.size(); j++) {
                matrix.get(indexes.get(j).getKey()).set(indexes.get(j).getValue(), values.get(j));
            }
        }
    }

    private static List<Pair<Integer, Integer>> borderIndexes(int m, int n, final int padding) {
        List<Pair<Integer, Integer>> list = new ArrayList<>();
        // top
        int lastY = n - padding - 1;
        int lastX = m - padding - 1;

        for (int i = padding; i < lastY; i++) {
            list.add(new Pair<>(padding, i));
        }
        // right
        for (int i = padding; i < lastX; i++) {
            list.add(new Pair<>(i, lastY));
        }
        // bottom
        for (int i = lastY; i > padding; i--) {
            list.add(new Pair<>(lastX, i));
        }
        // left
        for (int i = lastX; i > padding; i--) {
            list.add(new Pair<>(i, padding));
        }
        return list;
    }

    private static List<Integer> indexedValues(List<List<Integer>> matrix, List<Pair<Integer, Integer>> indexes) {
        List<Integer> values = new ArrayList<>(indexes.size());
        for (Pair<Integer, Integer> index : indexes) {
            values.add(matrix.get(index.getKey()).get(index.getValue()));
        }
        return values;
    }

    private static void traceMatrix(List<List<Integer>> matrix, String message) {
        if (!TRACE) {
            return;
        }

        Optional.ofNullable(message)
                .ifPresent(System.out::println);

        System.out.println(message);
        for (List<Integer> row : matrix) {
            for (Integer integer : row) {
                System.out.print(String.format("%1$3s", String.valueOf(integer)));
            }
            System.out.println();
        }
    }
}
