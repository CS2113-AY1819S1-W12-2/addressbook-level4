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
        this.prefixValue = prefixValue;
    }
}
