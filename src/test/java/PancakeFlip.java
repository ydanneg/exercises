import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;

public class PancakeFlip {


    @Test
    public void testFlip() {
        assertFlip(new int[] {1, 2, 3, 4, 5, 6}, 6, new int[] {6, 5, 4, 3, 2, 1});
        assertFlip(new int[] {1, 2, 3, 4, 5, 6}, 3, new int[] {3, 2, 1, 4, 5, 6});
        assertFlip(new int[] {1, 2, 3, 4, 5, 6}, 2, new int[] {2, 1, 3, 4, 5, 6});
        assertFlip(new int[] {1, 2, 3, 4, 5, 6}, 0, new int[] {1, 2, 3, 4, 5, 6});
        assertFlip(new int[] {1, 2, 3, 4, 5, 6}, 8, new int[] {6, 5, 4, 3, 2, 1});
        assertFlip(new int[] {1, 2, 3, 4, 5}, 5, new int[] {5, 4, 3, 2, 1});
        assertFlip(new int[] {1, 2, 3, 4, 5}, 4, new int[] {4, 3, 2, 1, 5});
        assertFlip(new int[] {1, 2}, 2, new int[] {2, 1});
    }

    @Test
    public void testPancakeSort() {
        assertPancakeFlip(new int[] {5, 2, 7, 3}, new int[] {2, 3, 5, 7});
        assertPancakeFlip(new int[] {1, 8, 3, 6, 2, 9}, new int[] {1, 2, 3, 6, 8, 9});
        assertPancakeFlip(new int[] {1, 2, 3, 4, 5}, new int[] {1, 2, 3, 4, 5});
        assertPancakeFlip(new int[] {2, 1, 3}, new int[] {1, 2, 3});
        assertPancakeFlip(new int[] {2, 1}, new int[] {1, 2});
        assertPancakeFlip(new int[] {1, 2}, new int[] {1, 2});

    }

    private void pancakeSort(int[] arr) {
        int sortedElementsNum = 0;

        do {
            int minIndex = 0;
            for (int i = 0; i < arr.length - sortedElementsNum; i++) {
                if (arr[i] < arr[minIndex]) {
                    minIndex = i;
                }
            }
            flip(arr, minIndex + 1);
            flip(arr, arr.length - sortedElementsNum);
            sortedElementsNum++;
        } while (sortedElementsNum < arr.length);

        flip(arr, arr.length);
    }

    private static void flip(int[] arr, int k) {
        int num = Math.min(arr.length, k);
        if (arr.length >= 2 && arr.length >= num) {
            for (int i = 0; i < num / 2; i++) {
                int temp = arr[num - i - 1];
                arr[num - i - 1] = arr[i];
                arr[i] = temp;
            }
        }
    }

    private void assertFlip(int[] arr, int k, int[] expected) {
        flip(arr, k);
        assert Arrays.equals(arr, expected);
    }

    private void assertPancakeFlip(int[] arr, int[] expected) {
        pancakeSort(arr);
        assertThat(arr, is(equalTo(expected)));
    }
}
