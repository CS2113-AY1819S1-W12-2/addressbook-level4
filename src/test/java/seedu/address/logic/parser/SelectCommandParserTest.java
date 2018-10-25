package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_LIST_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_LIST_SIX;
import static seedu.address.testutil.TypicalIndexes.INDEX_LIST_THREE;

import org.junit.Test;

import seedu.address.logic.commands.SelectCommand;

/**
 * Test scope: similar to {@code DeleteCommandParserTest}.
 * @see DeleteCommandParserTest
 */
public class SelectCommandParserTest {

    private SelectCommandParser parser = new SelectCommandParser();
    private final String invalidMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE);

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", invalidMessage);
    }

    //@@author lekoook
    @Test
    public void parse_validArgs_returnsSelectCommandSingle() {
        assertParseSuccess(parser, "1", new SelectCommand(INDEX_LIST_FIRST));
    }

    @Test
    public void parse_validArgs_returnSelectCommandMultiple() {
        assertParseSuccess(parser, "1 2 3", new SelectCommand(INDEX_LIST_THREE));
    }

    @Test
    public void parse_validArgs_returnSelectCommandRange() {
        assertParseSuccess(parser, "1 - 3", new SelectCommand(INDEX_LIST_THREE));
    }

    @Test
    public void parse_validArgs_returnSelectCommandMultipleRange() {
        assertParseSuccess(parser, "1  -  3 , 5 -7", new SelectCommand(INDEX_LIST_SIX));
    }

    @Test
    public void parse_invalidSingleNegative_throwsParseException() {
        assertParseFailure(parser, "-1", invalidMessage);
    }

    @Test
    public void parse_invalidMultipleNegative_throwsParseException() {
        assertParseFailure(parser, "-1 -2 3", invalidMessage);
    }

    @Test
    public void parse_invalidRangeFormat_throwsParseException() {
        assertParseFailure(parser, "1 -- 3", invalidMessage);
    }

    @Test
    public void parse_invalidRangeIndex_throwsParseException() {
        assertParseFailure(parser, "-3 - 3", invalidMessage);
    }
}
