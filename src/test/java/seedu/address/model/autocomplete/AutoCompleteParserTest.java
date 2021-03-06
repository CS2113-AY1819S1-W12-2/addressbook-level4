//@@author lekoook
package seedu.address.model.autocomplete;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seedu.address.logic.parser.CliSyntax;

public class AutoCompleteParserTest {

    @Test
    public void parseCommand_validArgs_returnValidParserPair() {
        AutoCompleteParserPair actualPair = AutoCompleteParser.parseCommand("list t/friends");
        AutoCompleteParserPair expectedPair = new AutoCompleteParserPair(CliSyntax.PREFIX_TAG, "friends");

        assertEquals(expectedPair, actualPair);
    }

    @Test
    public void parseCommand_invalidArgs_returnInvalidParserPair() {
        AutoCompleteParserPair actualPair = AutoCompleteParser.parseCommand("");
        AutoCompleteParserPair expectedPair =
                new AutoCompleteParserPair(CliSyntax.PREFIX_INVALID, CliSyntax.PREFIX_INVALID.getPrefix());

        assertEquals(expectedPair, actualPair);
    }
}
