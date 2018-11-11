package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import seedu.address.commons.core.index.Index;

/**
 * Helper functions for handling strings.
 */
public class StringUtil {

    /**
     * Regex strings for parsing.
     */
    private static final String REGEX_INTEGERS = "\\d+";
    private static final String REGEX_INDEX_RANGE_FORMAT =
            "(?s)(\\d+\\s*-\\s*\\d+\\s*\\s*,?)?(\\s*,\\s*\\d+\\s*-\\s*\\d+\\s*\\s*,?)*";

    /**
     * Returns true if the {@code sentence} contains the {@code word}.
     *   Ignores case, but a full word match is required.
     *   <br>examples:<pre>
     *       containsWordIgnoreCase("ABc def", "abc") == true
     *       containsWordIgnoreCase("ABc def", "DEF") == true
     *       containsWordIgnoreCase("ABc def", "AB") == false //not a full word match
     *       </pre>
     * @param sentence cannot be null
     * @param word cannot be null, cannot be empty, must be a single word
     */
    public static boolean containsWordIgnoreCase(String sentence, String word) {
        requireNonNull(sentence);
        requireNonNull(word);

        String preppedWord = word.trim();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");
        checkArgument(preppedWord.split("\\s+").length == 1, "Word parameter should be a single word");

        String preppedSentence = sentence;
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");

        return Arrays.stream(wordsInPreppedSentence)
                .anyMatch(preppedWord::equalsIgnoreCase);
    }

    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t) {
        requireNonNull(t);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if {@code s} represents a non-zero unsigned integer
     * e.g. 1, 2, 3, ..., {@code Integer.MAX_VALUE} <br>
     * Will return false for any other non-null string input
     * e.g. empty string, "-1", "0", "+1", and " 2 " (untrimmed), "3 0" (contains whitespace), "1 a" (contains letters)
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isNonZeroUnsignedInteger(String s) {
        requireNonNull(s);

        try {
            int value = Integer.parseInt(s);
            return value > 0 && !s.startsWith("+"); // "+1" is successfully parsed by Integer#parseInt(String)
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    //@@author lekoook

    /**
     * Tokenizes a string input with white spaces into a list of {@code Index}.
     *
     * @param input the user string input.
     * @return the list of tokenized {@code Index}.
     */
    public static ArrayList<Index> tokenizeIndexWithSpace(String input) {
        ArrayList<Index> output = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(input);
        while (tokenizer.hasMoreTokens()) {
            Index zeroBasedIndex = Index.fromOneBased(Integer.valueOf(tokenizer.nextToken()));
            output.add(zeroBasedIndex);
        }
        return output;
    }

    /**
     * Tokenizes a string input with the range delimiter into a list of {@code Index}.
     *
     * @param input the user string input.
     * @return the list of tokenized {@code Index}.
     */
    public static ArrayList<Index> tokenizeIndexWithRange(String input) {
        ArrayList<Index> output = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(input, "- \t\n\r\f");

        // Do a token count check here

        int start = Integer.valueOf(tokenizer.nextToken());
        int end = Integer.valueOf(tokenizer.nextToken());

        for (int i = start; i <= end; i++) {
            output.add(Index.fromOneBased(i));
        }

        return output;
    }

    /**
     * Determines if a string contains non zero unsigned {@code Index} only.
     *
     * @param input the user input string.
     * @return true if contains, false otherwise.
     */
    public static boolean areNonZeroUnsignedInteger(String input) {
        requireNonNull(input);

        StringTokenizer tokenizer = new StringTokenizer(input);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (!token.matches(REGEX_INTEGERS) || Integer.valueOf(token) <= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if a user input conforms to the {@code Index} range selection format with the help of regex.
     *
     * @param input the user input string.
     * @return true if conforms, false otherwise.
     */
    public static boolean isRangeIndexFormat(String input) {
        return input.trim().matches(REGEX_INDEX_RANGE_FORMAT);
    }

    /**
     * Determines if a user input has a valid select command format.
     *
     * @param input the user input string.
     * @return true if confirms, false otherwise.
     */
    public static boolean isValidMultipleIndexFormat(String input) {
        if (isRangeIndexFormat(input) || areNonZeroUnsignedInteger(input)) {
            return true;
        }
        return false;
    }
}
