import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * https://www.hackerrank.com/challenges/string-function-calculation/problem
 */
public class StringFunctionCalculation {

    @Test
    public void test() {
        assertThat(calculateMaxValue("aaaaaa"), is(12));
        assertThat(calculateMaxValue("abcabcddd"), is(9));
        assertThat(calculateMaxValue(""), is(0));
        assertThat(calculateMaxValue("a"), is(1));
    }

    private int calculateMaxValue(String t) {
        Set<String> subStrings = new LinkedHashSet<>();
        for (int i = 0; i < t.length(); i++) {
            for (int j = i + 1; j <= t.length(); j++) {
                subStrings.add(t.substring(i, j));
            }
        }
        int maxValue = 0;
        for (String subString : subStrings) {
            maxValue = Math.max(maxValue, subString.length() * totalMatches(t, subString));
        }
        return maxValue;
    }

    private int totalMatches(String t, String sub) {
        Matcher matcher = Pattern.compile(sub).matcher(t);
        int matches = 0;
        int i = 0;
        while (matcher.find(i)) {
            matches++;
            i = matcher.start() + 1;
        }
        return matches;
    }
}
