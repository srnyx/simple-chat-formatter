package xyz.srnyx.simplechatformatter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;


/**
 * Custom implementation of the {@link org.apache.commons.text.similarity.JaroWinklerSimilarity Jaro-Winkler similarity} algorithm
 * <p><i>Doesn't use {@link org.apache.commons.lang3.StringUtils#equals(CharSequence, CharSequence)}</i>
 */
public class SimpleSimilarity {
    @NotNull
    public static Double apply(@NotNull String left, @NotNull String right) {
        if (left.equals(right)) return 1.0;
        final int[] mtp = matches(left, right);
        final double m = mtp[0];
        if (m == 0) return 0d;
        final double j = (m / left.length() + m / right.length() + (m - (double) mtp[1] / 2) / m) / 3;
        return j < 0.7d ? j : j + 0.1 * mtp[2] * (1d - j);
    }

    /**
     * This method returns the Jaro-Winkler string matches, half transpositions, prefix array.
     *
     * @param   first   the first string to be matched
     * @param   second  the second string to be matched
     * @return          mtp array containing: matches, half transpositions, and prefix
     */
    @Contract("_, _ -> new")
    private static int[] matches(@NotNull String first, @NotNull String second) {
        final CharSequence max;
        final CharSequence min;
        if (first.length() > second.length()) {
            max = first;
            min = second;
        } else {
            max = second;
            min = first;
        }
        final int range = Math.max(max.length() / 2 - 1, 0);
        final int[] matchIndexes = new int[min.length()];
        Arrays.fill(matchIndexes, -1);
        final boolean[] matchFlags = new boolean[max.length()];
        int matches = 0;
        for (int mi = 0; mi < min.length(); mi++) {
            final char c1 = min.charAt(mi);
            for (int xi = Math.max(mi - range, 0), xn = Math.min(mi + range + 1, max.length()); xi < xn; xi++) if (!matchFlags[xi] && c1 == max.charAt(xi)) {
                matchIndexes[mi] = xi;
                matchFlags[xi] = true;
                matches++;
                break;
            }
        }
        final char[] ms1 = new char[matches];
        final char[] ms2 = new char[matches];
        for (int i = 0, si = 0; i < min.length(); i++) if (matchIndexes[i] != -1) {
            ms1[si] = min.charAt(i);
            si++;
        }
        for (int i = 0, si = 0; i < max.length(); i++) if (matchFlags[i]) {
            ms2[si] = max.charAt(i);
            si++;
        }
        int halfTranspositions = 0;
        for (int mi = 0; mi < ms1.length; mi++) if (ms1[mi] != ms2[mi]) halfTranspositions++;
        int prefix = 0;
        for (int mi = 0; mi < Math.min(4, min.length()); mi++) {
            if (first.charAt(mi) != second.charAt(mi)) break;
            prefix++;
        }
        return new int[]{matches, halfTranspositions, prefix};
    }
}
