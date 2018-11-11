//@@author lekoook
package seedu.address.model.autocomplete;

import seedu.address.logic.parser.Prefix;

/**
 * Pair object used in parsing of commands for auto complete functionality
 */
public class AutoCompleteParserPair {
    private final Prefix prefixType;
    private final String prefixValue;

    public AutoCompleteParserPair(Prefix prefixType, String prefixValue) {
        this.prefixType = prefixType;
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

    public Prefix getPrefixType() {
        return prefixType;
    }

    public String getPrefixValue() {
        return prefixValue;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof AutoCompleteParserPair
                && this.prefixType.equals(((AutoCompleteParserPair) other).prefixType)
                && this.prefixValue.equals(((AutoCompleteParserPair) other).prefixValue);
    }
}
