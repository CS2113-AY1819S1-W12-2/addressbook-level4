//@@author lekoook
package seedu.address.model.autocomplete;

import static org.junit.Assert.assertEquals;
import static seedu.address.logic.parser.CliSyntax.COMMAND_FIND;
import static seedu.address.logic.parser.CliSyntax.COMMAND_LIST;
import static seedu.address.logic.parser.CliSyntax.COMMAND_MAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INVALID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import org.junit.Test;

import seedu.address.logic.parser.ArgumentMultimap;

public class AutoCompleteArgumentsParserTest {

    private ArgumentMultimap argumentMultimap = new ArgumentMultimap();

    @Test
    public void parse_nonEmptyValidArgs_returnValidPrefix() {
        argumentMultimap.put(PREFIX_NAME, "john");
        AutoCompleteParserPair actualFindPair =
                AutoCompleteArgumentsParser.parse(COMMAND_FIND, " n/john", argumentMultimap);
        AutoCompleteParserPair expectedFindPair = new AutoCompleteParserPair(PREFIX_NAME, "john");

        argumentMultimap.put(PREFIX_TAG, "friends");
        AutoCompleteParserPair actualListPair =
                AutoCompleteArgumentsParser.parse(COMMAND_LIST, " t/friends", argumentMultimap);
        AutoCompleteParserPair expectedListPair = new AutoCompleteParserPair(PREFIX_TAG, "friends");

        AutoCompleteParserPair actualMailPair =
                AutoCompleteArgumentsParser.parse(COMMAND_MAIL, " t/friends", argumentMultimap);
        AutoCompleteParserPair expectedMailPair = new AutoCompleteParserPair(PREFIX_TAG, "friends");

        assertEquals(expectedFindPair, actualFindPair);
        assertEquals(expectedListPair, actualListPair);
        assertEquals(expectedMailPair, actualMailPair);
    }

    @Test
    public void parse_emptyArgs_returnCommandPrefix() {
        AutoCompleteParserPair actualPair =
                AutoCompleteArgumentsParser.parse(COMMAND_FIND, "", argumentMultimap);
        AutoCompleteParserPair expectedPair = new AutoCompleteParserPair(PREFIX_COMMAND, COMMAND_FIND);

        assertEquals(expectedPair, actualPair);
    }

    @Test
    public void parse_unspecifiedArgsPrefix_returnDefaultPrefix() {
        AutoCompleteParserPair actualFindPair =
                AutoCompleteArgumentsParser.parse(COMMAND_FIND, "john", argumentMultimap);
        AutoCompleteParserPair expectedFindPair = new AutoCompleteParserPair(PREFIX_NAME, "john");

        AutoCompleteParserPair actualListPair =
                AutoCompleteArgumentsParser.parse(COMMAND_LIST, "john", argumentMultimap);
        AutoCompleteParserPair expectedListPair = new AutoCompleteParserPair(PREFIX_INVALID, "john");

        AutoCompleteParserPair actualMailPair =
                AutoCompleteArgumentsParser.parse(COMMAND_MAIL, "john", argumentMultimap);
        AutoCompleteParserPair expectedMailPair = new AutoCompleteParserPair(PREFIX_TAG, "john");

        assertEquals(expectedFindPair, actualFindPair);
        assertEquals(expectedListPair, actualListPair);
        assertEquals(expectedMailPair, actualMailPair);
    }

    @Test
    public void parse_invalidArgs_returnInvalidPrefix() {
        AutoCompleteParserPair actualPair =
                AutoCompleteArgumentsParser.parse("invalidCommand", "arg", argumentMultimap);
        AutoCompleteParserPair expectedPair = new AutoCompleteParserPair(PREFIX_INVALID, "arg");

        assertEquals(expectedPair, actualPair);
    }
}
