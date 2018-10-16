//@@author lekoook
package seedu.address.model.autocomplete;

import seedu.address.logic.parser.Prefix;

/**
 * Pair object used in parsing of commands for auto complete functionality
 */
public class AutoCompleteParserPair {
    public final Prefix predictionType;
    public final String prefixValue;

    public AutoCompleteParserPair(Prefix predictionType, String prefixValue) {
        this.predictionType = predictionType;
        this.prefixValue = removeLeadingWhitespace(prefixValue);
    }

    /**
     * Removes leading whitespace for correct text prediction.
     * @param input the user input to trim.
     * @return the trimmed input.
     */
    private String removeLeadingWhitespace(String input) {
        for (int index = 0; index < input.length(); index++) {
            if (!Character.isWhitespace(input.charAt(index))) {
                return input.substring(index);
            }
        }
        return input;
    }
}
