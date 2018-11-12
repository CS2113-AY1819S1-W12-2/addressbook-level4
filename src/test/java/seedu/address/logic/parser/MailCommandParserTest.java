//@@author lekoook
package seedu.address.logic.parser;

import static seedu.address.logic.commands.MailCommand.MailType.TYPE_ALL;
import static seedu.address.logic.commands.MailCommand.MailType.TYPE_GROUPS;
import static seedu.address.logic.commands.MailCommand.MailType.TYPE_SELECTION;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.MailCommand;

public class MailCommandParserTest {

    private MailCommandParser parser = new MailCommandParser();

    @Test
    public void parse_allPersons_success() {
        MailCommand expectedCommand = new MailCommand(TYPE_ALL);
        assertParseSuccess(parser, "mail " + CliSyntax.PREFIX_ALL.getPrefix(), expectedCommand);
    }

    @Test
    public void parse_selectedPersons_success() {
        MailCommand expectedCommand = new MailCommand(TYPE_SELECTION);
        assertParseSuccess(parser, "mail", expectedCommand);
    }

    @Test
    public void parse_selectedTags_success() {
        MailCommand expectedCommand = new MailCommand(TYPE_GROUPS, "");
        assertParseSuccess(parser, "mail " + CliSyntax.PREFIX_TAG, expectedCommand);
    }

    @Test
    public void parse_multipleArgs_throwException() {
        String expectedMessage = String.format(
                MailCommandParser.MESSAGE_MULTIPLE_ARGS,
                MailCommandParser.MESSAGE_CORRECT_EG,
                MailCommandParser.MESSAGE_WRONG_EG);
        assertParseFailure(parser, "mail t/boss all/", expectedMessage);
    }
}
