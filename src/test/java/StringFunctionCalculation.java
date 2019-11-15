import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.Test;

/**
 * https://www.hackerrank.com/challenges/string-function-calculation/problem
 */
public class StringFunctionCalculation {

    @Test
    public void test() throws IOException, URISyntaxException {
        assertCalculateMaxValue("aaaaaa", 12);
        assertCalculateMaxValue("abcabcddd", 9);
        assertCalculateMaxValue("", 0);
        assertCalculateMaxValue("a", 1);

        Path path = Paths.get(getClass().getClassLoader()
                                        .getResource("input01.txt").toURI());
        String s = new String(Files.readAllBytes(path)).trim();
        assertCalculateMaxValue(s, 822043);
    }

    private static void assertCalculateMaxValue(CharSequence input, int expectedMaxValue) {
        System.out.println("input length: " + input.length());
        long time = System.currentTimeMillis();

        int result = calculateMaxValue(input);

        System.out.println("result: " + result);
        System.out.println("time: " + (System.currentTimeMillis() - time) + " ms");
        System.out.println();

        assertThat(result, is(expectedMaxValue));
    }

    static int calculateMaxValue(CharSequence input) {
        int result = 0;
        if (input.length() != 0) {
            int[] suffixArray = suffixArray(input);
            int[] lcp = lcp(suffixArray, input);
            for (int i = 0; i < suffixArray.length; i++) {
                result = Math.max(result, calculateMaxValue(suffixArray, lcp, i));
            }
        }
        return result;
    }

    static int calculateMaxValue(int[] suffixedArray, int[] lcp, int pos) {
        int suffixIndex = suffixedArray[pos];
        int suffixLen = suffixedArray.length - suffixIndex;
        int uniqueSubs = suffixLen - (pos > 0 ? lcp[pos - 1] : 0);
        int result = 0;

        for (int j = 0; j < uniqueSubs; j++) {
            int subLen = (suffixIndex + suffixLen - j) - suffixIndex;
            int i = pos;
            int occurs = 1;
            while (i < lcp.length && lcp[i] >= subLen) {
                occurs++;
                i++;
            }
            result = Math.max(result, occurs * subLen);
        }
        return result;
    }

    // sort suffixes of input in O(n*log(n))
    static int[] suffixArray(CharSequence input) {
        int n = input.length();
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            order[i] = n - 1 - i;
        }

        // stable sort of characters
        Arrays.sort(order, Comparator.comparingInt(input::charAt));

        int[] sa = new int[n];
        int[] classes = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = order[i];
            classes[i] = input.charAt(i);
        }

        // sa[i] - suffix on i'th position after sorting by first len characters
        // classes[i] - equivalence class of the i'th suffix after sorting by first len characters

        for (int len = 1; len < n; len *= 2) {
            int[] c = classes.clone();
            for (int i = 0; i < n; i++) {
                // condition sa[i - 1] + len < n simulates 0-symbol at the end of the string
                // a separate class is created for each suffix followed by simulated 0-symbol
                int i1 = i > 0
                         && c[sa[i - 1]] == c[sa[i]]
                         && sa[i - 1] + len < n
                         && c[sa[i - 1] + len / 2] == c[sa[i] + len / 2]
                         ? classes[sa[i - 1]] : i;
                classes[sa[i]] = i1;
            }
            // Suffixes are already sorted by first len characters
            // Now sort suffixes by first len * 2 characters
            int[] cnt = new int[n];
            for (int i = 0; i < n; i++) {
                cnt[i] = i;
            }
            int[] s = sa.clone();
            for (int i = 0; i < n; i++) {
                // s[i] - order of suffixes sorted by first len characters
                // (s[i] - len) - order of suffixes sorted only by second len characters
                int s1 = s[i] - len;
                // sort only suffixes of length > len, others are already sorted
                if (s1 >= 0) {
                    sa[cnt[classes[s1]]++] = s1;
                }
            }
        }
        return sa;
    }

    // longest common prefixes array in O(n)
    static int[] lcp(int[] sa, CharSequence s) {
        if (sa.length == 0) {
            return new int[0];
        }
        int n = sa.length;
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) { rank[sa[i]] = i; }
        int[] lcp = new int[n - 1];
        for (int i = 0, h = 0; i < n; i++) {
            if (rank[i] < n - 1) {
                for (int j = sa[rank[i] + 1]; Math.max(i, j) + h < s.length() && s.charAt(i + h) == s.charAt(j + h); ++h) { ; }
                lcp[rank[i]] = h;
                if (h > 0) { --h; }
            }
        }
        return lcp;
    }
}
