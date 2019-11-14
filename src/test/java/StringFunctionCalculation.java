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
        assertThat(calculateMaxValue("aaaaaa"), is(12));
        assertThat(calculateMaxValue("abcabcddd"), is(9));
        assertThat(calculateMaxValue(""), is(0));
        assertThat(calculateMaxValue("a"), is(1));

        Path path = Paths.get(getClass().getClassLoader()
                                        .getResource("input01.txt").toURI());
        String s = new String(Files.readAllBytes(path)).trim();
        assertThat(calculateMaxValue(s), is(822043));
    }

    //  method to return count of total distinct substring
    static int calculateMaxValue(String string) {
        if (string.length() == 0) {
            return 0;
        }
        SuffixedString txt = new SuffixedString(string);
        int max = f(txt, 0);
        for (int i = 1; i < txt.length(); i++) {
            max = Math.max(max, f(txt, i));
        }
        return max;
    }

    private static int f(SuffixedString suffixed, int pos) {
        int stringLen = suffixed.length();
        int suffixIndex = suffixed.suffixes[pos];
        int suffixLen = stringLen - suffixIndex;
        int cnt = suffixLen - (pos > 0 ? suffixed.lcp[pos - 1] : 0);
        int max = 0;

        for (int j = 0; j < cnt; j++) {
            int subLen = (suffixIndex + suffixLen - j) - suffixIndex;

            int i = pos;
            int occurs = 1;
            while (i < suffixed.lcp.length && suffixed.lcp[i] >= subLen) {
                occurs++;
                i++;
            }
            max = Math.max(max, occurs * subLen);
        }
        return max;
    }

    static class SuffixedString {

        String string;
        int[]  suffixes;
        int[]  lcp;

        private SuffixedString(String string) {
            this.string = string;
            this.suffixes = suffixArray(string);
            this.lcp = lcp(suffixes, string);
        }

        public int length() {
            return string.length();
        }

    }

    // sort suffixes of input in O(n*log(n))
    public static int[] suffixArray(CharSequence input) {
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
    public static int[] lcp(int[] sa, CharSequence s) {
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
