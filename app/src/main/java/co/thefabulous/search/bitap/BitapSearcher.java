package co.thefabulous.search.bitap;


import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.thefabulous.search.fuse.Options;

public class BitapSearcher implements Options.SearchFunction {
    private final String pattern;
    private final int patternLen;
    // Approximately where in the text is the pattern expected to be found?
    int location = 0;
    // Determines how close the match must be to the fuzzy location (specified above).
    // An exact letter match which is 'distance' characters away from the fuzzy location
    // would score as a complete mismatch. A distance of '0' requires the match be at
    // the exact location specified, a threshold of '1000' would require a perfect match
    // to be within 800 characters of the fuzzy location to be found using a 0.8 threshold.
    int distance = 100;
    // At what point does the match algorithm give up. A threshold of '0.0' requires a perfect match
    // (of both letters and location), a threshold of '1.0' would match anything.
    float threshold = 0.6f;
    // Machine word size
    int maxPatternLength = 32;
    private Options options;
    private int matchmask;
    private Map<Character, Integer> patternAlphabet;

    public BitapSearcher(String pattern, Options options) {
        this.options = new Options.Builder(options)
                .location(options.location != null ? options.location : location)
                .distance(options.distance != null ? options.distance : distance)
                .threshold(options.threshold != null ? options.threshold : threshold)
                .maxPatternLength(options.maxPatternLength != null ? options.maxPatternLength : maxPatternLength)
                .build();

        this.pattern = options.caseSensitive ? pattern : pattern.toLowerCase();
        this.patternLen = pattern.length();

        if (this.patternLen <= this.options.maxPatternLength) {
            this.matchmask = 1 << (this.patternLen - 1);
            this.patternAlphabet = calculatePatternAlphabet();
        }
    }

    public Map<Character, Integer> calculatePatternAlphabet() {
        Map<Character, Integer> s = new HashMap<>();
        char[] char_pattern = pattern.toCharArray();
        for (char c : char_pattern) {
            s.put(c, 0);
        }
        int i = 0;
        for (char c : char_pattern) {
            s.put(c, s.get(c) | (1 << (pattern.length() - i - 1)));
            i++;
        }
        return s;
    }

    @Override
    public Options.SearchResult search(String text) {
        Options options = this.options;
        int i;
        int j;
        int textLen;
        boolean findAllMatches;
        int location;
        double threshold;
        int bestLoc;
        int binMin;
        int binMid;
        int binMax;
        int start, finish;
        int[] bitArr;
        int[] lastBitArr = null;
        int charMatch;
        double score;
        ArrayList<Integer> locations;
        boolean isMatched = false;
        int[] matchMask;
        int matchesLen;
        String match;

        text = options.caseSensitive ? text : text.toLowerCase();

        if (this.pattern.equals(text)) {
            // Exact match
            return new BitapSearchResult.Builder()
                    .isMatch(true)
                    .score(0)
                    .matchedIndice(new Pair<>(0, text.length() - 1))
                    .build();
        }

        // When pattern length is greater than the machine word length, just do a a regex comparison
        if (this.patternLen > maxPatternLength) {
            WordTokenizer wordTokenizer = new WordTokenizer();
            Collection<String> apply = wordTokenizer.apply(pattern);
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (String item : apply) {
                if (!first || (first = false)) sb.append('|');
                sb.append(item);
            }
            String regEx = sb.toString();
            Pattern p = Pattern.compile(regEx);
            Matcher matcher = p.matcher(text);

            ArrayList<Pair<Integer, Integer>> matchedIndices = new ArrayList<>();
            while (matcher.find()) {
                match = matcher.group(0);
                matchedIndices.add(new Pair<>(text.indexOf(match), text.indexOf(match) + match.length() - 1));
            }

            // TODO: revisit this score
            isMatched = !matchedIndices.isEmpty();
            return new BitapSearchResult.Builder()
                    .isMatch(isMatched)
                    .score(isMatched ? 0.5 : 1)
                    .matchedIndices(matchedIndices)
                    .build();
        }

        findAllMatches = options.findAllMatches;

        location = options.location;
        // Set starting location at beginning text and initialize the alphabet.
        textLen = text.length();
        // Highest score beyond which we give up.
        threshold = options.threshold;
        // Is there a nearby exact match? (speedup)
        bestLoc = text.indexOf(pattern, location);

        // a mask of the matches
        matchMask = new int[textLen];
        for (i = 0; i < textLen; i++) {
            matchMask[i] = 0;
        }

        if (bestLoc != -1) {
            threshold = Math.min(this.bitapScore(0, bestLoc), threshold);
            // What about in the other direction? (speed up)
            bestLoc = text.lastIndexOf(this.pattern, location + this.patternLen);

            if (bestLoc != -1) {
                threshold = Math.min(this.bitapScore(0, bestLoc), threshold);
            }
        }

        bestLoc = -1;
        score = 1;
        locations = new ArrayList<>();
        binMax = this.patternLen + textLen;

        for (i = 0; i < this.patternLen; i++) {
            // Scan for the best match; each iteration allows for one more error.
            // Run a binary search to determine how far from the match location we can stray
            // at this error level.
            binMin = 0;
            binMid = binMax;
            while (binMin < binMid) {
                if (this.bitapScore(i, location + binMid) <= threshold) {
                    binMin = binMid;
                } else {
                    binMax = binMid;
                }
                binMid = (int) Math.floor((binMax - binMin) / 2 + binMin);
            }

            // Use the result from this iteration as the maximum for the next.
            binMax = binMid;
            start = Math.max(1, location - binMid + 1);
            if (findAllMatches) {
                finish = textLen;
            } else {
                finish = Math.min(location + binMid, textLen) + this.patternLen;
            }

            // Initialize the bit array
            bitArr = new int[finish + 2];

            bitArr[finish + 1] = (1 << i) - 1;

            for (j = finish; j >= start; j--) {
                if (j - 1 < text.length() && this.patternAlphabet.containsKey(text.charAt(j - 1))) {
                    charMatch = this.patternAlphabet.get(text.charAt(j - 1));
                } else {
                    charMatch = 0;
                }

                if (charMatch > 0) {
                    matchMask[j - 1] = 1;
                }

                if (i == 0) {
                    // First pass: exact match.
                    bitArr[j] = ((bitArr[j + 1] << 1) | 1) & charMatch;
                } else {
                    // Subsequent passes: fuzzy match.
                    bitArr[j] = ((bitArr[j + 1] << 1) | 1) & charMatch | (((lastBitArr[j + 1] | lastBitArr[j]) << 1) | 1) | lastBitArr[j + 1];
                }
                if ((bitArr[j] & this.matchmask) > 0) {
                    score = this.bitapScore(i, j - 1);

                    // This match will almost certainly be better than any existing match.
                    // But check anyway.
                    if (score <= threshold) {
                        // Indeed it is
                        threshold = score;
                        bestLoc = j - 1;
                        locations.add(bestLoc);

                        if (bestLoc > location) {
                            // When passing loc, don't exceed our current distance from loc.
                            start = Math.max(1, 2 * location - bestLoc);
                        } else {
                            // Already passed loc, downhill from here on in.
                            break;
                        }
                    }
                }
            }

            // No hope for a (better) match at greater error levels.
            if (this.bitapScore(i + 1, location) > threshold) {
                break;
            }
            lastBitArr = bitArr;
        }

        // Count exact matches (those with a score of 0) to be "almost" exact
        boolean isMatch = bestLoc >= 0;
        return new BitapSearchResult.Builder()
                .isMatch(isMatch)
                .score(score == 0 ? 0.001 : score)
                .matchedIndices(getMatchedIndices(matchMask))
                .build();
    }

    @Override
    public String pattern() {
        return pattern;
    }

    /**
     * Compute and return the score for a match with e errors and x location.
     *
     * @param errors   Number of errors in match.
     * @param location Location of match.
     * @return Overall score for match (0.0 = good, 1.0 = bad).
     */
    private double bitapScore(double errors, int location) {
        float accuracy = (float) (errors / this.patternLen);
        int proximity = Math.abs(this.options.location - location);

        if (this.options.distance == 0) {
            // Dodge divide by zero error.
            return proximity == 0 ? accuracy : 1.0;
        }
        return accuracy + (proximity / (float) this.options.distance);
    }

    private ArrayList<Pair<Integer, Integer>> getMatchedIndices(int[] matchMask) {
        ArrayList<Pair<Integer, Integer>> matchedIndices = new ArrayList<>();
        int start = -1;
        int end = -1;
        int i = 0;
        int match;
        int len = matchMask.length;
        for (; i < len; i++) {
            match = matchMask[i];
            if (match > 0 && start == -1) {
                start = i;
            } else if (match == 0 && start != -1) {
                end = i - 1;
                if ((end - start) + 1 >= this.options.minimumCharLength) {
                    matchedIndices.add(new Pair<>(start, end));
                }
                start = -1;
            }
        }
        if (matchMask[i - 1] > 0) {
            if ((i - 1 - start) + 1 >= this.options.minimumCharLength) {
                matchedIndices.add(new Pair<>(start, i - 1));
            }
        }
        return matchedIndices;
    }
}
